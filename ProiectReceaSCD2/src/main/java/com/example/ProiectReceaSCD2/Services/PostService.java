package com.example.ProiectReceaSCD2.Services;

import com.example.ProiectReceaSCD2.Entities.PostEntity;
import com.example.ProiectReceaSCD2.Entities.Status;
import com.example.ProiectReceaSCD2.Repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostEntity savePost(PostEntity postEntity){
        return postRepository.save(postEntity);
    }

    public PostEntity updatePost(Integer id, PostEntity updatedPost) {
        Optional<PostEntity> existingPostOpt = postRepository.findById(id);

        if (existingPostOpt.isPresent()) {
            PostEntity existingPost = existingPostOpt.get();
            existingPost.setTitle(updatedPost.getTitle());
            existingPost.setContent(updatedPost.getContent());
//            existingPost.setTitle(updatedPost.getStatus());

            return postRepository.save(existingPost);
        } else {
            throw new EntityNotFoundException("Post with id " + id + " not found");
        }
    }

    public void deletePost(Integer id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Post with id " + id + " not found");
        }
    }

    public PostEntity getPostById(Integer id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post with id " + id + " not found"));
    }

    public List<PostEntity> getAllPosts() {
        return postRepository.findAll();
    }

    public PostEntity updateStatus(Integer id, String status) {
        Optional<PostEntity> existingPostOpt = postRepository.findById(id);
        if (existingPostOpt.isPresent()) {
            PostEntity existingPost = existingPostOpt.get();
            try {
                // Convertește string-ul în enum folosind Status.valueOf()
                Status newStatus = Status.valueOf(status.toUpperCase()); // Asigură-te că string-ul este valid
                existingPost.setStatus(newStatus); // Setează status-ul ca enum
                return postRepository.save(existingPost); // Salvează modificările
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status value: " + status);
            }
        } else {
            throw new EntityNotFoundException("Post with ID " + id + " not found");
        }
    }

}
