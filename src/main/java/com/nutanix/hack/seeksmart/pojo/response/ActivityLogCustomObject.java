package com.nutanix.hack.seeksmart.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityLogCustomObject {
    private Long postId;
    private Integer ackCount;
    private Integer concurCount;
}
