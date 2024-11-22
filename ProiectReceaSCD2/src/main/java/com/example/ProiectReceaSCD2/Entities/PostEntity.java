package com.example.ProiectReceaSCD2.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Data
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String user_id;

    @Column(nullable = false)
    @CreationTimestamp
    private Date created_on;

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    private Status status;

}
