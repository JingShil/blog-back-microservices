package com.ccsu.article.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ccsu.article.entity.Article;
import com.ccsu.article.entity.ArticleListElement;


public interface ArticleService extends IService<Article> {
    public IPage<Article> getArticleList(Integer pageNumber, Integer pageSize, String userId, String tagId, String title, Integer published, Integer order);
    public IPage<Article> getArticles(ArticleListElement articleListElement);
}
