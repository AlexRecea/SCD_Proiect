package com.example.ProiectReceaSCD2.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer post_id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String user_id;

    @Column(nullable = false)
    private Date created_on;
}
