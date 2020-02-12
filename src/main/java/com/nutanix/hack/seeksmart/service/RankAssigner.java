package com.nutanix.hack.seeksmart.service;

import com.nutanix.hack.seeksmart.model.ActivityLog;
import com.nutanix.hack.seeksmart.model.Hot;
import com.nutanix.hack.seeksmart.model.Trending;
import com.nutanix.hack.seeksmart.pojo.response.ActivityLogCustomObject;
import com.nutanix.hack.seeksmart.repository.ActivityLogRepository;
import com.nutanix.hack.seeksmart.repository.HotRepository;
import com.nutanix.hack.seeksmart.repository.PostRepository;
import com.nutanix.hack.seeksmart.repository.TrendingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RankAssigner {
    private final HotRepository hotRepository;
    private final PostRepository postRepository;
    private final TrendingRepository trendingRepository;
    private final ActivityLogRepository activityLogRepository;
    
    private static final long millisIn5Minutes = 300000;
    private static final Long trendingTimeConstant = 2016L;

    @Scheduled(fixedDelay = millisIn5Minutes)
    public void rankPosts() {
        
        Long currentSeconds = System.currentTimeMillis();
        Long endTime = currentSeconds - (currentSeconds % millisIn5Minutes);
        Long startTime = endTime - millisIn5Minutes;
        
        List<ActivityLog> postActivityLog = activityLogRepository.findByIsConcurFalseAndTimeStampBetween(startTime, endTime);
        List<ActivityLog> concurActivityLog = activityLogRepository.findByIsConcurTrueAndTimeStampBetween(startTime, endTime);
    
        
        Map<Long, ActivityLogCustomObject> logMap = new HashMap<>();
        
        for(ActivityLog log : postActivityLog) {
            Long postId = log.getPostId();
            if(!logMap.containsKey(postId))
                logMap.put(postId, new ActivityLogCustomObject(postId, 0, 0));
            logMap.get(postId).setAckCount(logMap.get(postId).getAckCount() + 1);
        }
        for(ActivityLog log : concurActivityLog) {
            Long postId = log.getPostId();
            if (!logMap.containsKey(postId))
                logMap.put(postId, new ActivityLogCustomObject(postId, 0, 0));
            logMap.get(postId).setConcurCount(logMap.get(postId).getConcurCount() + 1);
        }
        
        for(ActivityLogCustomObject post : logMap.values()) {
            Hot hot = hotRepository.findHotByPostId(post.getPostId());
            Double newCycleRank;
            if(post.getConcurCount()==0)
                newCycleRank = 0.0;
            else if (post.getAckCount()==0)
                newCycleRank = 1.0;
            else
                newCycleRank = (post.getConcurCount()/post.getAckCount())*1.0;
            if(hot == null) {
                hot = Hot.builder()
                        .postId(post.getPostId())
                        .rank(newCycleRank)
                        .cycleCount(1)
                        .build();
                hotRepository.save(hot);
            }
            else {
                Double newRank = ((hot.getRank() * hot.getCycleCount()) + newCycleRank)/(hot.getCycleCount()+1);
                hot.setCycleCount(hot.getCycleCount()+1);
                hot.setRank(newRank);
                hotRepository.save(hot);
            }
        }
    
        for(ActivityLogCustomObject post : logMap.values()) {
            Trending trending = trendingRepository.findTrendingByPostId(post.getPostId());
            Double newCycleRank;
            if(post.getConcurCount()==0)
                newCycleRank = 0.0;
            else if (post.getAckCount()==0)
                newCycleRank = 1.0;
            else
                newCycleRank = (post.getConcurCount()/post.getAckCount())*1.0 + (1.0/trendingTimeConstant);
    
            if(trending == null) {
                trending = Trending.builder()
                        .postId(post.getPostId())
                        .rank(newCycleRank)
                        .cycleCount(1)
                        .build();
                trendingRepository.save(trending);
            }
            else {
                newCycleRank += 1.0*trending.getCycleCount()/trendingTimeConstant;
                Double newRank = ((trending.getRank() * trending.getCycleCount()) +
                        newCycleRank)/(trending.getCycleCount()+1);
                trending.setCycleCount(trending.getCycleCount()+1);
                trending.setRank(newRank);
                trendingRepository.save(trending);
            }
        }
    }
}
