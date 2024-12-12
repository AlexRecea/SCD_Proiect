package com.example.ProiectReceaSCD2.DTOs;

import com.example.ProiectReceaSCD2.Entities.PostEntity;
import com.example.ProiectReceaSCD2.Entities.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

public class PostDTO {

    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private String content;

    @Getter
    @Setter
    private Status status;

    @Getter
    @Setter
    private Date createdOn;

    @Getter
    @Setter
    private String userName;

    @Getter
    @Setter
    private List<CommentDTO> comments; // Lista de comentarii asociate postării

    public PostDTO() {}

    public PostDTO(PostEntity post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.status = post.getStatus();
        this.createdOn = post.getCreated_on();
        this.userName = post.getUser().getName();
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }


}
