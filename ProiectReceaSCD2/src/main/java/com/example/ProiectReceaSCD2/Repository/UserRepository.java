package com.example.ProiectReceaSCD2.Repository;

import com.example.ProiectReceaSCD2.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByNameAndEmail(String name, String email);

    Optional<Object> findByName(String name);
    Optional<UserEntity> findByEmail(String email);
}
