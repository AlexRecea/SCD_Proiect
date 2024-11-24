package com.example.ProiectReceaSCD2.Entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    @JsonIgnoreProperties("user") // Ignoră câmpul "user" din PostEntity pentru a preveni ciclurile infinite
    @JsonManagedReference
    private List<PostEntity> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments;
}
