package com.example.ProiectReceaSCD2.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class CommentDTO {

    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    private String content;

    @Getter
    @Setter
    private Date createdOn;

    @Getter
    @Setter
    private String userName; // Numele utilizatorului care a creat comentariul
}