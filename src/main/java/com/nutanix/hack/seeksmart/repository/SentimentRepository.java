package com.nutanix.hack.seeksmart.repository;

import com.nutanix.hack.seeksmart.model.Sentiment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SentimentRepository extends JpaRepository<Sentiment, Long> {

    List<Sentiment> findByTagEqualsAndTimestampBetween(int tag, Long startTime, Long endTime);
}
