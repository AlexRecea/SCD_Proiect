package com.example.ProiectReceaSCD2.Controllers;

import com.example.ProiectReceaSCD2.DTOs.DTOMapper;
import com.example.ProiectReceaSCD2.DTOs.PostDTO;
import com.example.ProiectReceaSCD2.Entities.PostEntity;
import com.example.ProiectReceaSCD2.Entities.Status;
import com.example.ProiectReceaSCD2.Entities.UserEntity;
import com.example.ProiectReceaSCD2.Repository.PostRepository;
import com.example.ProiectReceaSCD2.Repository.UserRepository;
import com.example.ProiectReceaSCD2.Services.PostService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("http://localhost:5173/")
public class PostController {

    @Autowired
    public final PostService postService;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PostRepository postRepository;

    public PostController(PostService postService, UserRepository userRepository, PostRepository postRepository) {
        this.postService = postService;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

//    @PostMapping("/createPost")
//    public ResponseEntity<?> createPost(@RequestBody Map<String, Object> request) {
//        try {
//            // Extrage datele din request
//            String title = (String) request.get("title");
//            String content = (String) request.get("content");
//            Integer userId = (Integer) request.get("user_id");
//
//            // Verifică dacă utilizatorul există
//            UserEntity user = userRepository.findById(userId)
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//
//            // Creează și salvează postarea
//            PostEntity postEntity = new PostEntity();
//            postEntity.setTitle(title);
//            postEntity.setContent(content);
//            postEntity.setUser(user);
//            postEntity.setStatus(Status.PENDING);
//
//            PostEntity savedPost = postRepository.save(postEntity);
//            return ResponseEntity.ok(savedPost);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error creating post: " + e.getMessage());
//        }
//    }

    @PostMapping("/createPost")
    public ResponseEntity<?> createPost(@RequestBody PostEntity postEntity) {
        UserEntity user = userRepository.findById(postEntity.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        postEntity.setUser(user);
        postRepository.save(postEntity);
        return ResponseEntity.ok("Post created successfully");
    }

    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<PostEntity> updateStatus(@PathVariable Integer id, @RequestBody String status) {
        // Elimină ghilimelele de la status dacă este trimis în format JSON
        status = status.replaceAll("^\"|\"$", "");

        // Verificăm dacă statusul este valid
        try {
            PostEntity updatedPost = postService.updateStatus(id, status);
            return ResponseEntity.ok(updatedPost);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deletePost/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getPost/{id}")
    public ResponseEntity<PostEntity> getPostById(@PathVariable Integer id) {
        PostEntity post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

//    @GetMapping("/getAllPosts")
//    public ResponseEntity<List<PostDTO>> getAllPosts() {
//        List<PostEntity> posts = postRepository.findAll();
//        List<PostDTO> postDTOs = posts.stream()
//                .map(PostDTO::new)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(postDTOs);
//    }

    @GetMapping("/getAllPosts")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> postDTOs = postRepository.findAll()
                .stream()
                .map(DTOMapper::mapToPostDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(postDTOs);
    }
}
