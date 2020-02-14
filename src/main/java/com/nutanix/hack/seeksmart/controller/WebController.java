package com.nutanix.hack.seeksmart.controller;

import com.nutanix.hack.seeksmart.model.Sentiment;
import com.nutanix.hack.seeksmart.pojo.response.Ponto;
import com.nutanix.hack.seeksmart.repository.SentimentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Controller
public class WebController {
    private final SentimentRepository sentimentRepository;


    @GetMapping(path = "/sentiment/analysis")
    public String getSentimentChart(ModelMap model) {
        List<Ponto> pontos = new ArrayList<>();
        List<Sentiment> sentiments = sentimentRepository.findByTagEqualsAndTimestampBetween(0,
                0L, Instant.now().toEpochMilli());
        for (Sentiment sentiment : sentiments) {
            pontos.add(new Ponto(sentiment.getTimestamp(), sentiment.getIndex()));
        }
        model.addAttribute("pontos",pontos);
        return "graph";
    }
}
