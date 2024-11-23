package com.example.ProiectReceaSCD2.Entities;

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

    @ManyToOne(fetch = FetchType.LAZY) // Relația Many-to-One către PostEntity
    @JoinColumn(name = "post_id", nullable = false) // FK în tabela Comment
    private PostEntity post;

    @ManyToOne(fetch = FetchType.LAZY) // Relația Many-to-One către UserEntity
    @JoinColumn(name = "user_id", nullable = false) // FK în tabela Comment
    private UserEntity user;

    @Column(nullable = false)
    @CreationTimestamp
    private Date created_on;
}
