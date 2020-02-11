package com.nutanix.hack.seeksmart.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "post")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rant_generator")
    @SequenceGenerator(name = "rant_generator", sequenceName = "rant_seq")
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "rant", nullable = false)
    private String rant;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = true;

    @Column(name = "no_of_acks", nullable = false)
    private Integer noOfAcks = 0;

    @Column(name = "no_of_concurs", nullable = false)
    private Integer noOfConcurs = 0;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Long createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Long updatedAt;

    @Column(name = "hash_tags", nullable = true)
    private List<String> hashTags;

}
