package com.ccsu.article.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccsu.article.constants.MqConstants;
import com.ccsu.article.dto.Result;
import com.ccsu.article.entity.*;
import com.ccsu.article.index.ArticleIndex;
import com.ccsu.article.repository.ArticleRepository;
import com.ccsu.article.service.ArticleService;
import com.ccsu.article.service.ArticleTagService;
import com.ccsu.article.service.TagService;
import com.ccsu.article.service.impl.ArticleIndexServiceImpl;
import com.ccsu.feign.clients.UserClient;
import com.ccsu.feign.entity.User;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleTagService articleTagService;



    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Autowired
    private TagService tagService;

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ArticleIndexServiceImpl articleIndexServiceImpl;

//    @PostMapping("/toEs")
//    public void toEs(@RequestBody ArticleListElement articleListElement) {
//        articleIndexServiceImpl.search(articleListElement);
//    }

//    @PostMapping("/toEs")
//    public Article toEs() {
//        Article article = articleIndexServiceImpl.getById("1");
//        return article;
//    }
//
//    @PostMapping("/addEs")
//    public Article addEs(@RequestBody Article article) {
//        articleIndexServiceImpl.saveOrUpdate(article);
//        Article article1 = articleIndexServiceImpl.getById(article.getId());
//        return article;
//    }
//
//    @PostMapping("/deleteEs")
//    public void deleteEs() {
//        articleIndexServiceImpl.delete("11458");
//
//    }

//    @PostMapping("/addEs")
//    public void addEs(){
//        Article article = articleService.getById("1");
//        ArticleIndex articleIndex = new ArticleIndex();
//        articleIndex.setArticle(article);
//        articleRepository.save(articleIndex);
//    }
//    @GetMapping("/getEs")
//    public String getEs(){
//
//
//        Iterable<ArticleIndex> articleRepositoryAll = articleRepository.findAll();
//        return articleRepositoryAll.toString();
////        IndexRequest request = new IndexRequest("personal_blog");
////        request.
////        GetRequest getRequest = new GetRequest();
////        getRequest.
////        GetResponse getResponse = new GetResponse();
//    }

    @PostMapping("/mysqlToES")
    public void mysqlToES(){

        List<Article> articleList = articleService.list();
        if(articleList!=null){

            for(Article article : articleList){

                articleIndexServiceImpl.saveOrUpdate(article);
            }
        }

//        Iterable<ArticleIndex> all = articleRepository.findAll();
//        return all.toString();
    }

//    @PostMapping("/testMq")
//    public void addTest(){
//        rabbitTemplate.convertAndSend(MqConstants.ARTICLE_EXCHANGE,MqConstants.ARTICLE_INSERT_KEY,"123");
//    }


    @PostMapping("/save")
    public Result<String> save(@RequestHeader("UserId") String userId, @RequestBody ArticleTags articleTags){

        List<Tag> tagList = articleTags.getTagList();
        Article article = articleTags.getArticle();
        article.setUserId(userId);
        if(article==null){
            return Result.error("保存错误");
        }
        if(article.getId() != null) {
            LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ArticleTag::getArticleId, article.getId());
            articleTagService.remove(queryWrapper);
        }else {
            String id = UUID.randomUUID().toString();
            article.setId(id);
        }

        List<ArticleTag> articleTagList = new ArrayList<>();

        if(tagList !=null)
            for (Tag tag : tagList){
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tag.getId());
                articleTagList.add(articleTag);
            }
        articleService.saveOrUpdate(article);
        articleTagService.saveBatch(articleTagList);
        rabbitTemplate.convertAndSend(MqConstants.ARTICLE_EXCHANGE,MqConstants.ARTICLE_INSERT_KEY,article.getId());
        return Result.success(article.getId());
    }

    @PostMapping("/list/private")
    public Result<IPage<Article>> listPrivate(@RequestHeader("UserId") String userId, @RequestBody ArticleListElement articleListElement){
//        String id = tokenUtil.extractSubjectFromToken(token);
        articleListElement.setUserId(userId);
        IPage<Article> articlePageList = articleService.getArticles(articleListElement);
        if(articlePageList == null){
            return Result.error("错误");
        }

        return Result.success(articlePageList);
    }

    @PostMapping("/list")
    public Result<IPage<Article>> list(@RequestBody ArticleListElement articleListElement){

        IPage<Article> articlePageList = articleIndexServiceImpl.search(articleListElement);
        if(articlePageList == null){
            return Result.error("错误");
        }

        return Result.success(articlePageList);
    }



    @GetMapping("/get-one")
    public Result<ArticleTags> getOne(@RequestParam(value = "articleId") String articleId){
        if(articleId==null){
            return Result.error("错误");
        }
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,articleId);
        List<ArticleTag> articleTagList = articleTagService.list(queryWrapper);
        List<String> tagIdList=new ArrayList<>();
        for(ArticleTag articleTag : articleTagList){
            tagIdList.add(articleTag.getTagId());
        }
        List<Tag> tagList = new ArrayList<>();
//        System.out.println(tagIdList.isEmpty());
        if(!tagIdList.isEmpty()){
            tagList = tagService.listByIds(tagIdList);
        }
        Article article = articleService.getById(articleId);
        ArticleTags articleTags = new ArticleTags();
        articleTags.setArticle(article);
        articleTags.setTagList(tagList);

        return Result.success(articleTags);
    }


    @DeleteMapping("/delete")
    public Result<String> delete(@RequestParam(value = "articleId") String articleId){
        LambdaQueryWrapper<ArticleTag> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,articleId);
        articleTagService.remove(queryWrapper);
        articleService.removeById(articleId);
        rabbitTemplate.convertAndSend(MqConstants.ARTICLE_EXCHANGE,MqConstants.ARTICLE_DELETE_KEY,articleId);
        return Result.success("删除成功");
    }

    @GetMapping("/public")
    public Result<String> publicArticle(@RequestParam(value = "articleId") Integer articleId){
        Article article = articleService.getById(articleId);
        if(article.getPublished()==1){
            return Result.error("该文章已发布");
        }else{
            article.setPublished(1);
            articleService.save(article);
            return Result.success("文章已发布");
        }

    }

    @GetMapping("/private")
    public Result<String> privateArticle(@RequestParam(value = "articleId") Integer articleId){
        Article article = articleService.getById(articleId);
        if(article.getPublished()==0){
            return Result.error("该文章没有发布");
        }else{
            article.setPublished(0);
            articleService.save(article);
            return Result.success("文章已取消发布");
        }

    }

    @PostMapping("/api/save")
    public void apiSaveArticle(@RequestBody Article article){

        articleService.saveOrUpdate(article);
        rabbitTemplate.convertAndSend(MqConstants.ARTICLE_EXCHANGE,MqConstants.ARTICLE_INSERT_KEY,article.getId());
    }

    @GetMapping("/api/get")
    public Article apiGetArticle(@RequestParam(value = "articleId") String articleId){
        Article article = articleService.getById(articleId);
        return article;
    }
}
