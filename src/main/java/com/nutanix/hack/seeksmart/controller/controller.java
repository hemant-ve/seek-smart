package com.nutanix.hack.seeksmart.controller;

import com.nutanix.hack.seeksmart.model.Hot;
import com.nutanix.hack.seeksmart.model.Post;
import com.nutanix.hack.seeksmart.model.Trending;
import com.nutanix.hack.seeksmart.pojo.request.CreatePostRequest;
import com.nutanix.hack.seeksmart.pojo.request.PostBulkRequest;
import com.nutanix.hack.seeksmart.pojo.response.PostItem;
import com.nutanix.hack.seeksmart.pojo.response.PostResponse;
import com.nutanix.hack.seeksmart.repository.HotRepository;
import com.nutanix.hack.seeksmart.repository.PostRepository;
import com.nutanix.hack.seeksmart.repository.TrendingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class controller {
    private final HotRepository hotRepository;
    private final PostRepository postRepository;
    private final TrendingRepository trendingRepository;

    @GetMapping(path = "/post")
    @ResponseStatus(HttpStatus.OK)
    public PostResponse getAllPost(@RequestParam(name = "keyset", defaultValue = "0") Long keySet) {
        List<Post> posts = postRepository.findTop50ByCreatedAtAfterAndCreatedAtBeforeAAndIsDeletedFalseOrderByCreatedAtDesc(keySet, Instant.now().toEpochMilli());
        List<PostItem> postItems = posts.stream()
                .map(i -> new PostItem(i.getRant(), i.getCreatedAt(), i.getNoOfAcks(), i.getNoOfConcurs(), i.getCreatedBy()))
                .collect(Collectors.toList());
        return new PostResponse(postItems, postItems.get(postItems.size() - 1).getCreatedAt());
    }

    @GetMapping(path = "/post/trending/")
    @ResponseStatus(HttpStatus.OK)
    public PostResponse getAllTrendingPost(@RequestParam(name = "keyset", defaultValue = "0") Integer keySet) {
        if(keySet == 0) {
            keySet = Integer.MAX_VALUE;
        }
        List<Trending> trendingList = trendingRepository.findTop50ByRankAfterAndRankBeforeOrderByRank(keySet, Integer.MIN_VALUE);
        List<Long> postIds = trendingList.stream().map(Trending::getPostId).collect(Collectors.toList());
        List<Post> posts = postRepository.findAllById(postIds);
        List<PostItem> postItems = posts.stream()
                .map(i -> new PostItem(i.getRant(), i.getCreatedAt(), i.getNoOfAcks(), i.getNoOfConcurs(), i.getCreatedBy()))
                .collect(Collectors.toList());
        return new PostResponse(postItems, postItems.get(postItems.size() - 1).getCreatedAt());
    }

    @GetMapping(path = "/post/hot/")
    @ResponseStatus(HttpStatus.OK)
    public PostResponse getAllhotPost(@RequestParam(name = "keyset", defaultValue = "0") Integer keySet) {
        if(keySet == 0) {
            keySet = Integer.MAX_VALUE;
        }
        List<Hot> trendingList = hotRepository.findTop50ByRankAfterAndRankBeforeOrderByRank(keySet, Integer.MIN_VALUE);
        List<Long> postIds = trendingList.stream().map(Hot::getPostId).collect(Collectors.toList());
        List<Post> posts = postRepository.findAllById(postIds);
        List<PostItem> postItems = posts.stream()
                .map(i -> new PostItem(i.getRant(), i.getCreatedAt(), i.getNoOfAcks(), i.getNoOfConcurs(), i.getCreatedBy()))
                .collect(Collectors.toList());
        return new PostResponse(postItems, postItems.get(postItems.size() - 1).getCreatedAt());
    }

    @PostMapping(path = "/post")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createPost(@Valid @RequestBody(required = true) CreatePostRequest createPostRequest) {
        Post post = Post.builder()
                .rant(createPostRequest.getRant())
                .createdBy(createPostRequest.getUserName())
                .isDeleted(false)
                .build();
        post = postRepository.save(post);
        return post.getId();
    }

    @DeleteMapping(path = "/post/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Long deletePost(@PathVariable(name = "id") Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if(post.isPresent()) {
            Post postObj = post.get();
            postObj.setIsDeleted(true);
            postRepository.save(postObj);
            return postObj.getId();
        }
        return 0L;
    }

    @PutMapping(path = "/post/{id}/concur")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Long concurPost(@PathVariable(name = "id") Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if(post.isPresent()) {
            Post postObj = post.get();
            postObj.setNoOfAcks(postObj.getNoOfAcks()+1);
            postRepository.save(postObj);
            return postObj.getId();
        }
        return 0L;
    }

    @PutMapping(path = "/post/{id}/ack")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void ackPost(@Valid @RequestBody PostBulkRequest postBulkRequest) {
        List<Post> posts = postRepository.findAllById(postBulkRequest.getPostIds());
        for (Post post : posts) {
            post.setNoOfAcks(post.getNoOfAcks()+1);
            postRepository.save(post);
        }
    }

}
