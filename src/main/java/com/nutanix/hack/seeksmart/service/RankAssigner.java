package com.nutanix.hack.seeksmart.service;

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
import java.util.List;

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
    
        List<ActivityLogCustomObject> activityLogs = activityLogRepository.
                findActivitiesInTimeRange(startTime,endTime);
        
        for(ActivityLogCustomObject post : activityLogs) {
            Hot hot = hotRepository.findHotByPostId(post.getPostId());
            Double newCycleRank = (post.getConcurCount()/post.getAckCount())*1.0;
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
    
        for(ActivityLogCustomObject post : activityLogs) {
            Trending trending = trendingRepository.findTrendingByPostId(post.getPostId());
            Double newCycleRank = null;
            if(trending == null) {
                newCycleRank = (post.getConcurCount()/post.getAckCount())*1.0 + (1/trendingTimeConstant);
                trending = Trending.builder()
                        .postId(post.getPostId())
                        .rank(newCycleRank)
                        .cycleCount(1)
                        .build();
                trendingRepository.save(trending);
            }
            else {
                newCycleRank = (post.getConcurCount()/post.getAckCount())*1.0 +
                        ((trending.getCycleCount()+1)/trendingTimeConstant);
                Double newRank = ((trending.getRank() * trending.getCycleCount()) +
                        newCycleRank)/(trending.getCycleCount()+1);
                trending.setCycleCount(trending.getCycleCount()+1);
                trending.setRank(newRank);
                trendingRepository.save(trending);
            }
        }
    }
}
