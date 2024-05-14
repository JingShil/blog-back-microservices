package com.ccsu.article.mq;

import com.ccsu.article.constants.MqConstants;
import com.ccsu.article.entity.Article;
import com.ccsu.article.index.ArticleIndex;
import com.ccsu.article.repository.ArticleRepository;
import com.ccsu.article.service.ArticleService;
import com.ccsu.article.service.impl.ArticleIndexServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.index.qual.SameLen;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ArticleListener {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleIndexServiceImpl articleIndexService;


    @RabbitListener(queues = MqConstants.ARTICLE_INSERT_QUEUE)
    public void listenerArticleInsertOrUpdate(String id){

        Article article = articleService.getById(id);
        articleIndexService.saveOrUpdate(article);
        log.info("已保存" + id);
    }

    @RabbitListener(queues = MqConstants.ARTICLE_DELETE_QUEUE)
    public void listenerArticleDelete(String id){
        articleIndexService.delete(id);
        log.info("已删除" + id);
    }
}
