package com.ccsu.article.entity;

import com.ccsu.article.index.ArticleIndex;
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

    public void setArticleIndex(ArticleIndex article){
        this.id=article.getId();
        this.userId= article.getUserId();
        this.title= article.getTitle();
        this.content = article.getContent();
        this.published = article.getPublished();
        this.likeNum = article.getLikeNum();
        this.collectNum = article.getCollectNum();
        this.viewNum = article.getViewNum();
        this.commentNum = article.getCommentNum();
        this.createTime = article.getCreateTime();
        this.updateTime = article.getUpdateTime();
        this.img = article.getImg();
    }
}
