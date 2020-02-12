package com.nutanix.hack.seeksmart.repository;

import com.nutanix.hack.seeksmart.model.ActivityLog;
import com.nutanix.hack.seeksmart.pojo.response.ActivityLogCustomObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
     
     Optional<ActivityLog> findTopByOrderByTimeStampAsc();
     
     @Query("SELECT avg(p.sentimentIndex) FROM ActivityLog a, Post p WHERE a.postId = p.id AND a.isConcur = true AND a.timeStamp BETWEEN ?1 AND ?2")
     float findAvgConcurIndexBetweenTimestamp(@Param("startTime") Long startTime, @Param("endTime") Long endTime);
     
     @Query("SELECT a.postId as postId , count(a.id) as ackCount , sum(CASE WHEN a.isConcur = TRUE THEN 1 ELSE 0 END) as concurCount FROM ActivityLog a  where a.timeStamp between ?1 and ?2 group by a.postId")
     List<ActivityLogCustomObject> findActivitiesInTimeRange(@Param("startTime") Long startTime, @Param("endTime") Long endTime);
}
