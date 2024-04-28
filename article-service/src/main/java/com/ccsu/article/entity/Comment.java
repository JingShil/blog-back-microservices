package com.ccsu.article.entity;

import lombok.Data;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Data
public class Comment {
    private String id;
    private String articleId;
    private String userId;
    private String content;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate(){
        createTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onupdate(){
        updateTime=LocalDateTime.now();
    }
}
