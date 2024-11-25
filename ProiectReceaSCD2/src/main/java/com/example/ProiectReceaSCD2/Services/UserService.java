package com.example.ProiectReceaSCD2.Services;

import com.example.ProiectReceaSCD2.DTOs.UserDTO;
import com.example.ProiectReceaSCD2.Entities.UserEntity;
import com.example.ProiectReceaSCD2.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity createUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserDTO findUserByNameAndEmail(String name, String email) {
        UserEntity user = userRepository.findByNameAndEmail(name, email)
                .orElseThrow(() -> new RuntimeException("User not found with given name and email"));

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());

        return userDTO;
    }
}
