package com.nutanix.hack.seeksmart.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;

@Entity
@Table(name = "activity_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "log_generator")
    @SequenceGenerator(name = "log_generator", sequenceName = "log_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "is_concur", nullable = false)
    private Boolean isConcur = false;

    @CreatedDate
    @Column(name = "time_stamp", nullable = false, updatable = false)
    private Long timeStamp;
}
