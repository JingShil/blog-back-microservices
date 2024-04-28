package com.ccsu.article.entity;

import lombok.Data;

@Data
public class ArticleListElement {
    private Integer pageNumber;
    private Integer pageSize;
    private String userId;
    private String tagId;
    private String title;
    private Integer published;
    private Integer order;
}
