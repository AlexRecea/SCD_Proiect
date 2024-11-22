package com.example.ProiectReceaSCD2.Controllers;

import com.example.ProiectReceaSCD2.Entities.CommentEntity;
import com.example.ProiectReceaSCD2.Services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:5173/")
public class CommentController {

    @Autowired
    public final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/addComment")
    public CommentEntity createComment(@RequestBody CommentEntity commentEntity){
        return commentService.addComment(commentEntity);
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
