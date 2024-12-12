package com.example.ProiectReceaSCD2.Repository;

import com.example.ProiectReceaSCD2.Entities.PostEntity;
import com.example.ProiectReceaSCD2.Entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Integer> {
    List<PostEntity> findByUserId(Integer userId);

    List<PostEntity> findByUserIdAndStatus(Integer user_id, Status status);
}
