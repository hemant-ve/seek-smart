package com.nutanix.hack.seeksmart.repository;

import com.nutanix.hack.seeksmart.model.Hot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotRepository extends JpaRepository<Hot, Long> {

    List<Hot> findTop50ByRankAfterAndRankBeforeOrderByRankAsc(Integer keySet, int minValue);
}
