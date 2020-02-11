package com.nutanix.hack.seeksmart.controller;

import com.nutanix.hack.seeksmart.model.Hot;
import com.nutanix.hack.seeksmart.model.Post;
import com.nutanix.hack.seeksmart.model.Trending;
import com.nutanix.hack.seeksmart.pojo.response.PostItem;
import com.nutanix.hack.seeksmart.pojo.response.PostResponse;
import com.nutanix.hack.seeksmart.repository.HotRepository;
import com.nutanix.hack.seeksmart.repository.PostRepository;
import com.nutanix.hack.seeksmart.repository.TrendingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
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
        List<Post> posts = postRepository.findTop50ByCreatedAtAfterAndCreatedAtBeforeOrderByCreatedAtDesc(keySet, Instant.now().toEpochMilli());
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
    @ResponseStatus(HttpS)
}
