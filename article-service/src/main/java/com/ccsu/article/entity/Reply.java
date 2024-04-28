package com.ccsu.article.entity;

import lombok.Data;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Data
public class Reply {
    private String id;
    private String commentId;
    private String parentId;
    private String userId;
    private String content;
    private LocalDateTime updateTime;
    private LocalDateTime createTime;
    @PrePersist
    protected void onCreate(){
        createTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onupdate(){
        updateTime=LocalDateTime.now();
    }
}
