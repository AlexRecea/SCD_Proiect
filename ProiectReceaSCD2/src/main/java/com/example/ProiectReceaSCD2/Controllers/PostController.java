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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
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

    @GetMapping("/getAllPosts")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> postDTOs = postRepository.findAll()
                .stream()
                .map(DTOMapper::mapToPostDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(postDTOs);
    }

    @GetMapping("/getMyPosts")
    public ResponseEntity<List<PostDTO>> getMyPosts(@RequestParam Integer userId) {
        // Verificare dacă userId este null sau invalid
        if (userId == null) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
        // Găsirea postărilor pentru utilizatorul specificat cu status "PUBLISHED"
        List<PostEntity> posts = postRepository.findByUserIdAndStatus(userId, Status.PUBLISHED);

        // Verificare dacă lista de postări este goală
        if (posts.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        // Convertirea în DTO-uri
        List<PostDTO> postDTOs = posts.stream()
                .map(post -> new PostDTO(post))
                .collect(Collectors.toList());

        // Returnarea listei de DTO-uri
        return ResponseEntity.ok(postDTOs);
    }

    @PutMapping("/updatePost")
    public ResponseEntity<Object> updatePost(@RequestBody PostDTO postDTO) {
        return postRepository.findById(postDTO.getId())
                .map(existingPost -> {
                    existingPost.setTitle(postDTO.getTitle());
                    existingPost.setContent(postDTO.getContent());
                    postRepository.save(existingPost);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
