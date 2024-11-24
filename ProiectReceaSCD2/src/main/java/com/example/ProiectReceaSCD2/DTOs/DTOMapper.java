package com.example.ProiectReceaSCD2.DTOs;

import com.example.ProiectReceaSCD2.Entities.CommentEntity;
import com.example.ProiectReceaSCD2.Entities.PostEntity;
import com.example.ProiectReceaSCD2.Entities.Status;

import java.util.List;
import java.util.stream.Collectors;

public class DTOMapper {
    public static PostDTO mapToPostDTO(PostEntity post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());
        postDTO.setStatus(Status.valueOf(post.getStatus().toString()));
        postDTO.setCreatedOn(post.getCreated_on());
        postDTO.setUserName(post.getUser().getName());
        postDTO.setComments(
                post.getComments().stream()
                        .map(DTOMapper::mapToCommentDTO)
                        .collect(Collectors.toList())
        );
        return postDTO;
    }

    public static CommentDTO mapToCommentDTO(CommentEntity comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreatedOn(comment.getCreated_on());
        commentDTO.setUserName(comment.getUser().getName());
        return commentDTO;
    }
}