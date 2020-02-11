package com.nutanix.hack.seeksmart.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "index")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sentiment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sentiment_generator")
    @SequenceGenerator(name = "sentiment_generator", sequenceName = "sentiment_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    
    @Column(name = "timestamp", nullable = false)
    private Long timestamp;
    
    @Column(name = "index", nullable = false)
    private float index;
    
    @Column(name = "tag", nullable = false)
    private int tag = 0;
}
