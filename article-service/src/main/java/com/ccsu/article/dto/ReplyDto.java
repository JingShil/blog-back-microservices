package com.ccsu.article.dto;


import com.ccsu.article.entity.Reply;
import lombok.Data;

@Data
public class ReplyDto {
    private String userName;
    private String userAvatar;
    private Reply reply;
}
