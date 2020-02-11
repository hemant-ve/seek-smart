package com.nutanix.hack.seeksmart.repository;

import com.nutanix.hack.seeksmart.model.Trending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrendingRepository extends JpaRepository<Trending, Long> {
    
    public Trending findTrendingByPostId(Long postId);
    public List<Trending> findTop50ByRankAfterAndRankBeforeOrderByRankAsc(Double rankAfter, Double rankBefore);
}
