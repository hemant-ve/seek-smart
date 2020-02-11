package com.nutanix.hack.seeksmart.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "trending")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trending {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trending_generator")
    @SequenceGenerator(name = "trending_generator", sequenceName = "trending_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "rank", nullable = false)
    private Integer rank;
}
