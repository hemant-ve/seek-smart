package com.nutanix.hack.seeksmart.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostRequest {
    @NotNull
    private String rant;
    @NotNull
    private String userName;
}
