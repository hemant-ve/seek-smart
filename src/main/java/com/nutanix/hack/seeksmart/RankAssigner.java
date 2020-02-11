package com.nutanix.hack.seeksmart;

import com.nutanix.hack.seeksmart.repository.HotRepository;
import com.nutanix.hack.seeksmart.repository.PostRepository;
import com.nutanix.hack.seeksmart.repository.TrendingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RankAssigner {
    private final HotRepository hotRepository;
    private final PostRepository postRepository;
    private final TrendingRepository trendingRepository;

    @Scheduled(fixedDelay = 300000L)
    public void rankPosts() {

    }
}
