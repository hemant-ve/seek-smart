package com.nutanix.hack.seeksmart.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SentimentResponse {
    private Long timestamp;
    private float index;
}
