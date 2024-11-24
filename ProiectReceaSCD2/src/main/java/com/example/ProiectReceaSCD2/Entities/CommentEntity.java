package com.example.ProiectReceaSCD2.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
//    @JsonIgnoreProperties("comments") // Ignoră câmpul "comments" din PostEntity pentru a preveni ciclurile infinite
    private PostEntity post;

    @ManyToOne
    @JoinColumn(name = "user_id")
//    @JsonIgnoreProperties("comments") // Ignoră câmpul "comments" din UserEntity pentru a preveni ciclurile infinite
    private UserEntity user;

    @Column(nullable = false)
    @CreationTimestamp
    private Date created_on;
}
