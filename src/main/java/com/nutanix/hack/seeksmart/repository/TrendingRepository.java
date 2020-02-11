package com.nutanix.hack.seeksmart.repository;

import com.nutanix.hack.seeksmart.model.Hot;
import com.nutanix.hack.seeksmart.model.Trending;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrendingRepository extends JpaRepository<Trending, Long> {
    public List<Trending> findTop50ByRankAfterAndRankBeforeOrderByRank(Integer rankAfter, Integer rankBefore);
    
    Trending findTrendingByPostId(Long postId);
}
