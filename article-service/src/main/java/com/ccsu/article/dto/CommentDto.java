package com.ccsu.article.dto;


import com.ccsu.article.entity.Comment;
import lombok.Data;

@Data
public class CommentDto {
    private String userName;
    private String userAvatar;
    private Comment comment;
}
