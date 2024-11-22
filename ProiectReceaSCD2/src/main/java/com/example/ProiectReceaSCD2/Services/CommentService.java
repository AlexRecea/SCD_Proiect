package com.example.ProiectReceaSCD2.Services;

import com.example.ProiectReceaSCD2.Entities.CommentEntity;
import com.example.ProiectReceaSCD2.Repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public CommentEntity addComment(CommentEntity commentEntity){
        return commentRepository.save(commentEntity);
    }

    public CommentEntity updateComment(Integer id, CommentEntity updatedComment) {
        Optional<CommentEntity> existingCommentOpt = commentRepository.findById(id);

        if (existingCommentOpt.isPresent()) {
            CommentEntity existingComment = existingCommentOpt.get();
            existingComment.setContent(updatedComment.getContent());
            // Setează alte câmpuri care necesită actualizare, dacă există

            return commentRepository.save(existingComment);
        } else {
            throw new EntityNotFoundException("Comment with id " + id + " not found");
        }
    }

    public void deleteComment(Integer id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Comment with id " + id + " not found");
        }
    }

    public List<CommentEntity> getAllComments() {
        return commentRepository.findAll();
    }

}
