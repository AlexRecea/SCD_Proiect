package com.example.ProiectReceaSCD2.Controllers;

import com.example.ProiectReceaSCD2.DTOs.UserDTO;
import com.example.ProiectReceaSCD2.Entities.UserEntity;
import com.example.ProiectReceaSCD2.Repository.UserRepository;
import com.example.ProiectReceaSCD2.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:5173")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @PostMapping("/createUser")
//    public ResponseEntity<?> createUser(@RequestBody UserEntity userEntity) {
//        System.out.println("Received user data: " + userEntity);
//        try {
//            UserEntity savedUser = userRepository.save(userEntity);
//            return ResponseEntity.ok(savedUser);
//        } catch (Exception e) {
//            System.out.println("Error: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user");
//        }
//    }
    @PostMapping("/createUser")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user){
        UserEntity createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String name = loginData.get("name");
        String email = loginData.get("email");

        Optional<UserEntity> foundUser = userRepository.findByNameAndEmail(name, email);

        if (foundUser.isPresent()) {
            return ResponseEntity.ok(foundUser.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }


    @GetMapping("/getUserId")
    public ResponseEntity<Integer> getUserId(@RequestParam String name, @RequestParam String email) {
        try {
            UserDTO userDTO = userService.findUserByNameAndEmail(name, email);
            return ResponseEntity.ok(userDTO.getId());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
