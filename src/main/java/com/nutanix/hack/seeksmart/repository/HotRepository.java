package com.nutanix.hack.seeksmart.repository;

import com.nutanix.hack.seeksmart.model.Hot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotRepository extends JpaRepository<Hot, Long> {
    
    Hot findHotByPostId(Long postId);
    List<Hot> findTop50ByRankAfterAndRankBeforeOrderByRankAsc(Double keySet, Double minValue);

}
