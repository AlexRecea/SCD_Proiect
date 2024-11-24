package com.example.ProiectReceaSCD2.Repository;

import com.example.ProiectReceaSCD2.Entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    List<CommentEntity> findByPostId(Integer postId);

}
