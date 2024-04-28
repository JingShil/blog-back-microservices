package com.ccsu.article.entity;

import lombok.Data;

import java.util.List;

@Data
public class PageArticleTag {
    private List<ArticleTags> articleTagsList;
    private Long total;
}
