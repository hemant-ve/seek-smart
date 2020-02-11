package com.nutanix.hack.seeksmart.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostItem {
    private String rant;
    private Long createdAt;
    private Integer noOfAcks;
    private Integer noOfConcurs;
    private String createdBy;
}
