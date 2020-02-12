package com.nutanix.hack.seeksmart.controller;

import com.nutanix.hack.seeksmart.model.*;
import com.nutanix.hack.seeksmart.pojo.request.CreatePostRequest;
import com.nutanix.hack.seeksmart.pojo.request.PostBulkRequest;
import com.nutanix.hack.seeksmart.pojo.request.TimeRange;
import com.nutanix.hack.seeksmart.pojo.response.PostItem;
import com.nutanix.hack.seeksmart.pojo.response.PostResponse;
import com.nutanix.hack.seeksmart.pojo.response.SentimentResponse;
import com.nutanix.hack.seeksmart.repository.*;
import com.nutanix.hack.seeksmart.service.SentimentAnalyzer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class controller {
    private final HotRepository hotRepository;
    private final PostRepository postRepository;
    private final TrendingRepository trendingRepository;
    private final SentimentRepository sentimentRepository;
    private final SentimentAnalyzer sentimentAnalyzer;
    private final ActivityLogRepository activityLogRepository;

    @GetMapping(path = "/post")
    @ResponseStatus(HttpStatus.OK)
    public PostResponse getAllPost(@RequestParam(name = "keyset", defaultValue = "0") Long keySet) {
        List<Post> posts = postRepository.findTop50ByCreatedAtAfterAndCreatedAtBeforeAndIsDeletedFalseOrderByCreatedAtDesc(keySet, Instant.now().toEpochMilli());
        List<PostItem> postItems = posts.stream()
                .map(i -> new PostItem(i.getRant(), i.getCreatedAt(), i.getNoOfAcks(), i.getNoOfConcurs(), i.getCreatedBy()))
                .collect(Collectors.toList());
        return new PostResponse(postItems, postItems.get(postItems.size() - 1).getCreatedAt());
    }

    @GetMapping(path = "/post/trending/")
    @ResponseStatus(HttpStatus.OK)
    public PostResponse getAllTrendingPost(@RequestParam(name = "keyset", defaultValue = "0.0") Double keySet) {
        if(keySet == 0) {
            keySet = Double.MAX_VALUE;
        }
        List<Trending> trendingList = trendingRepository.findTop50ByRankAfterAndRankBeforeOrderByRankAsc(keySet, Double.MIN_VALUE);
        List<Long> postIds = trendingList.stream().map(Trending::getPostId).collect(Collectors.toList());
        List<Post> posts = postRepository.findAllById(postIds);
        List<PostItem> postItems = posts.stream()
                .map(i -> new PostItem(i.getRant(), i.getCreatedAt(), i.getNoOfAcks(), i.getNoOfConcurs(), i.getCreatedBy()))
                .collect(Collectors.toList());
        return new PostResponse(postItems, postItems.get(postItems.size() - 1).getCreatedAt());
    }

    @GetMapping(path = "/post/hot/")
    @ResponseStatus(HttpStatus.OK)
    public PostResponse getAllhotPost(@RequestParam(name = "keyset", defaultValue = "0.0") Double keySet) {
        if(keySet == 0.0) {
            keySet = Double.MAX_VALUE;
        }
        List<Hot> trendingList = hotRepository.findTop50ByRankAfterAndRankBeforeOrderByRankAsc(keySet, Double.MIN_VALUE);
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
        Pattern hashTagPattern = Pattern.compile("#(\\S+)");
        Matcher mat = hashTagPattern.matcher(createPostRequest.getRant());
        List<String> hashTags = new ArrayList<>();
        while (mat.find()) {
            hashTags.add(mat.group(1));
        }
        Post post = Post.builder()
                .rant(createPostRequest.getRant())
                .sentimentIndex(sentimentAnalyzer.findSentiment(createPostRequest.getRant()))
                .createdBy(createPostRequest.getUserName())
                .isDeleted(false)
                .hashTags(hashTags)
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
            ActivityLog activityLog = ActivityLog.builder()
                    .postId(postObj.getId())
                    .isConcur(true)
                    .build();
            activityLogRepository.save(activityLog);
            return postObj.getId();
        }
        return 0L;
    }

    @PutMapping(path = "/post/ack")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void ackPost(@Valid @RequestBody PostBulkRequest postBulkRequest) {
        List<Post> posts = postRepository.findAllById(postBulkRequest.getPostIds());
        for (Post post : posts) {
            post.setNoOfAcks(post.getNoOfAcks()+1);
            postRepository.save(post);
            ActivityLog activityLog = ActivityLog.builder()
                    .postId(post.getId())
                    .isConcur(false)
                    .build();
            activityLogRepository.save(activityLog);
        }
    }
    
    @GetMapping(path = "/sentiment")
    @ResponseStatus(HttpStatus.OK)
    public List<SentimentResponse> getSentimentTimeseries(@RequestParam(name = "timeRange") TimeRange timeRange) {
        List<Sentiment> sentiments = sentimentRepository.findByTagEqualsAndTimestampBetween(0,
                timeRange.getStartTime(), timeRange.getEndTime());
        return sentiments.stream().map(sentiment -> new SentimentResponse(sentiment.getTimestamp(), sentiment.getIndex()))
                .collect(Collectors.toList());
    }

}
