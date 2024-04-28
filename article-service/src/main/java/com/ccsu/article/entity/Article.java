package com.ccsu.article.entity;

import lombok.Data;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Data
public class Article {
    private String id;
    private String userId;
    private String title;
    private String content;
    private Integer published;
    private Integer likeNum;
    private Integer collectNum;
    private Integer viewNum;
    private Integer commentNum;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String img;
    @PrePersist
    protected void onCreate(){
        createTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onupdate(){
        updateTime=LocalDateTime.now();
    }
}
