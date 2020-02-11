package com.nutanix.hack.seeksmart.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostBulkRequest {
    @NotNull
    List<Long> postIds;
}
