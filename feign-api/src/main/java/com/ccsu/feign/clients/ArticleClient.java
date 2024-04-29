package com.ccsu.feign.clients;

import com.ccsu.feign.dto.Result;
import com.ccsu.feign.entity.Article;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("article-service")
public interface ArticleClient {

    @PostMapping("/article/api/save")
    public void apiSaveArticle(@RequestBody Article article);

    @GetMapping("/article/api/get")
    public Article apiGetArticle(@RequestParam(value = "articleId") String articleId);

}
