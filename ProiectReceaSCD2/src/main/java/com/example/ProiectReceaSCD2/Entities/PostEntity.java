package com.example.ProiectReceaSCD2.Entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private UserEntity user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
//    @JsonIgnoreProperties("post") // Ignoră câmpul "post" din CommentEntity
    private List<CommentEntity> comments;

    @Column(nullable = false)
    @CreationTimestamp
    private Date created_on;

    @Enumerated(EnumType.STRING)
    private Status status;

    @PrePersist
    private void setDefaultStatus() {
        if (status == null) {
            status = Status.PENDING; // Setează valoarea implicită PENDING
        }
    }

}
