package com.nutanix.hack.seeksmart.service;

import com.nutanix.hack.seeksmart.model.ActivityLog;
import com.nutanix.hack.seeksmart.model.Sentiment;
import com.nutanix.hack.seeksmart.repository.ActivityLogRepository;
import com.nutanix.hack.seeksmart.repository.PostRepository;
import com.nutanix.hack.seeksmart.repository.SentimentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SentimentCron {
    private final PostRepository postRepository;
    private final SentimentRepository sentimentRepository;
    private final ActivityLogRepository activityLogRepository;
    
    private static final int CRON_RATE_MINUTES = 5;
    private static final int BATCH_SIZE_MINUTES = 5;
    private static final float CONCUR_WEIGHTS = 0.1F;
    
    @Scheduled(fixedRate = CRON_RATE_MINUTES*60*1000)
    public void sentimentTimeseriesCron() {
        Long currentTimestamp = Instant.now().toEpochMilli();
        Optional<Sentiment> lastSentimentObject = sentimentRepository.findTopByTagOrderByTimestampDesc(0);
        Optional<ActivityLog> firstActivityLogObject = activityLogRepository.findTopByOrderByTimeStampAsc();
        Long nextBatchTimestamp;
        if(lastSentimentObject.isPresent())
            nextBatchTimestamp = lastSentimentObject.get().getTimestamp() + TimeUnit.MINUTES.toMillis(5);
        else if (firstActivityLogObject.isPresent()) {
            nextBatchTimestamp = firstActivityLogObject.get().getTimeStamp();
        } else {
            return;
        }
        
        for ( ; nextBatchTimestamp < currentTimestamp; nextBatchTimestamp = nextBatchTimestamp + TimeUnit.MINUTES.toMillis(BATCH_SIZE_MINUTES)) {
            Sentiment sentiment = getPeriodSentiment(nextBatchTimestamp, 0);
            sentimentRepository.save(sentiment);
        }
    }
    
    private Sentiment getPeriodSentiment(Long nextBatchTimestamp, int tag) {
        float postIndex = postRepository.findAvgPostIndexBetweenTimestamp(nextBatchTimestamp - TimeUnit.MINUTES.toMillis(BATCH_SIZE_MINUTES), nextBatchTimestamp);
        float concurIndex = activityLogRepository.findAvgConcurIndexBetweenTimestamp(nextBatchTimestamp - TimeUnit.MINUTES.toMillis(BATCH_SIZE_MINUTES), nextBatchTimestamp);
        return Sentiment.builder()
                .timestamp(nextBatchTimestamp)
                .index((1-CONCUR_WEIGHTS)*postIndex + CONCUR_WEIGHTS*concurIndex)
                .tag(tag).build();
    }
}
