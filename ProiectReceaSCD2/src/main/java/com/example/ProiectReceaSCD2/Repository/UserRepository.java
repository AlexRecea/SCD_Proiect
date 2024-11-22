package com.example.ProiectReceaSCD2.Repository;

import com.example.ProiectReceaSCD2.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
}
