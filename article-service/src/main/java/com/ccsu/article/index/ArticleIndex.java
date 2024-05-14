package com.ccsu.article.index;


import com.alibaba.fastjson.annotation.JSONField;
import com.ccsu.article.entity.Article;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Data
@Document(indexName = "article_index")
public class ArticleIndex {
    @Id
    private String id;
    private String userId;
    private String title;
    private String content;
    private Integer published;
    private Integer likeNum;
    private Integer collectNum;
    private Integer viewNum;
    private Integer commentNum;


    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "uuuu-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "uuuu-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    private String img;

//    public ArticleIndex(Article article){
//        this.id=article.getId();
//        this.userId= article.getUserId();
//        this.title= article.getTitle();
//        this.content = article.getContent();
//        this.published = article.getPublished();
//        this.likeNum = article.getLikeNum();
//        this.collectNum = article.getCollectNum();
//        this.viewNum = article.getViewNum();
//        this.commentNum = article.getCommentNum();
//        this.createTime = article.getCreateTime();
//        this.updateTime = article.getUpdateTime();
//        this.img = article.getImg();
//    }
    public void setArticle(Article article){
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
