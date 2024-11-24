package com.example.ProiectReceaSCD2.Repository;

import com.example.ProiectReceaSCD2.Entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Integer> {
}
