package com.example.ProiectReceaSCD2.Controllers;

import com.example.ProiectReceaSCD2.Entities.PostEntity;
import com.example.ProiectReceaSCD2.Entities.Status;
import com.example.ProiectReceaSCD2.Services.PostService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:5173/")
public class PostController {

    @Autowired
    public final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/createPost")
    public PostEntity createPost(@RequestBody PostEntity postEntity) {
        return postService.savePost(postEntity);
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

    @GetMapping("/getAllPosts")
    public ResponseEntity<List<PostEntity>> getAllPosts() {
        List<PostEntity> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }
}
