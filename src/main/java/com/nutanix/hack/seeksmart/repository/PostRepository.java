package com.nutanix.hack.seeksmart.repository;

import com.nutanix.hack.seeksmart.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT avg(p.sentimentIndex) FROM Post p WHERE p.isDeleted = false AND p.createdAt BETWEEN ?1 AND ?2")
    float findAvgPostIndexBetweenTimestamp(@Param("startTime") Long startTime, @Param("endTime") Long endTime);
    
    List<Post> findTop50ByCreatedAtAfterAndCreatedAtBeforeAndIsDeletedFalseOrderByCreatedAtDesc(Long createdByAfter, Long createdByBefore);
}
