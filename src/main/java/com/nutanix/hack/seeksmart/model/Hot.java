package com.nutanix.hack.seeksmart.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "hot")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hot {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hot_generator")
    @SequenceGenerator(name = "hot_generator", sequenceName = "hot_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "rank", nullable = false)
    private Double rank;
    
    @Column(name = "cycle_count", nullable = false)
    private Integer cycleCount;
}
