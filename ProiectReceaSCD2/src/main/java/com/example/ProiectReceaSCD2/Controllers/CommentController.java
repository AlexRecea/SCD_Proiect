package com.example.ProiectReceaSCD2.Controllers;

import com.example.ProiectReceaSCD2.Entities.CommentEntity;
import com.example.ProiectReceaSCD2.Entities.PostEntity;
import com.example.ProiectReceaSCD2.Entities.UserEntity;
import com.example.ProiectReceaSCD2.Repository.CommentRepository;
import com.example.ProiectReceaSCD2.Repository.PostRepository;
import com.example.ProiectReceaSCD2.Repository.UserRepository;
import com.example.ProiectReceaSCD2.Services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:5173/")
public class CommentController {

    @Autowired
    public final CommentService commentService;

    @Autowired
    public final PostRepository postRepository;

    @Autowired
    public final UserRepository userRepository;

    @Autowired
    public final CommentRepository commentRepository;

    public CommentController(CommentService commentService, PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.commentService = commentService;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @PostMapping("/addComment")
    public ResponseEntity<?> addComment(@RequestBody Map<String, Object> payload) {
        try {
            // Extrage datele din payload
            String content = (String) payload.get("content");
            Integer postId = (Integer) payload.get("postId");
            Integer userId = (Integer) payload.get("userId");

            // Găsește postarea și utilizatorul
            PostEntity post = postRepository.findById(postId)
                    .orElseThrow(() -> new RuntimeException("Post not found"));
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Creează comentariul
            CommentEntity comment = new CommentEntity();
            comment.setContent(content);
            comment.setPost(post);
            comment.setUser(user);

            // Salvează comentariul
            commentRepository.save(comment);

            return ResponseEntity.ok("Comment added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error adding comment: " + e.getMessage());
        }
    }

    @PutMapping("/updateComment/{id}")
    public CommentEntity updateComment(@RequestBody CommentEntity commentEntity, @PathVariable Integer id){
        return commentService.updateComment(id, commentEntity);
    }

    @DeleteMapping("/deleteComment/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer id){
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAllComment")
    public ResponseEntity<List<CommentEntity>> getAllComment(){
        List<CommentEntity> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }
}
