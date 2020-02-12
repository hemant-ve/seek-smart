package com.nutanix.hack.seeksmart.repository;

import com.nutanix.hack.seeksmart.model.ActivityLog;
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
     Float findAvgConcurIndexBetweenTimestamp(@Param("startTime") Long startTime, @Param("endTime") Long endTime);
    
     List<ActivityLog> findByIsConcurFalseAndTimeStampBetween(@Param("startTime") Long startTime, @Param("endTime") Long endTime);
    List<ActivityLog> findByIsConcurTrueAndTimeStampBetween(@Param("startTime") Long startTime, @Param("endTime") Long endTime);
}
