package com.nutanix.hack.seeksmart.repository;

import com.nutanix.hack.seeksmart.model.Trending;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrendingRepository extends JpaRepository<Trending, Long> {
    
    public Trending findTrendingByPostId(Long postId);
    public List<Trending> findTop50ByRankAfterAndRankBeforeOrderByRankAsc(Double rankAfter, Double rankBefore);
}
