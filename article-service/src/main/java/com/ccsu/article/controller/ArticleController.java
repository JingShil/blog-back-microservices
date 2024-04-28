package com.ccsu.article.controller;

import com.ccsu.article.clients.UserClient;
import com.ccsu.article.entity.Article;
import com.ccsu.article.entity.User;
import com.ccsu.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserClient userClient;

    @GetMapping("/get")
    public Article get(@RequestParam String id){
        Article article = articleService.getById(id);
        return article;
    }

    @GetMapping("/getUser")
    public User getUser(@RequestParam String userId){
        User byId = userClient.findById(userId);
        return byId;
    }
}
