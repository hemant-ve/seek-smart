package com.nutanix.hack.seeksmart.repository;

import com.nutanix.hack.seeksmart.model.Sentiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SentimentRepository extends JpaRepository<Sentiment, Long> {

    List<Sentiment> findByTagEqualsAndTimestampBetween(int tag, Long startTime, Long endTime);
    Sentiment findTopByTagOrderByTimestampDesc(int tag);
}
