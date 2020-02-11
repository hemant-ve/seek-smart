package com.nutanix.hack.seeksmart.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeRange {
    private Long startTime;
    private Long endTime;
}
