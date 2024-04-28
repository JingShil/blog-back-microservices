package com.ccsu.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ccsu.article.entity.Article;
import com.ccsu.article.entity.ArticleListElement;
import com.ccsu.article.entity.ArticleTag;
import com.ccsu.article.mapper.ArticleMapper;
import com.ccsu.article.service.ArticleService;
import com.ccsu.article.service.ArticleTagService;
import com.ccsu.article.utils.canstant.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleTagService articleTagService;

    @Override
    public IPage<Article> getArticles(ArticleListElement articleListElement){
        if(articleListElement==null){
            return null;
        }
//        MPJLambdaWrapper<Article> mpjLambdaWrapper=new MPJLambdaWrapper<>();

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        if(articleListElement.getUserId() != null){
            queryWrapper.eq(Article::getUserId,articleListElement.getUserId());
        }
        if(articleListElement.getPublished() != null){
            queryWrapper.eq(Article::getPublished,articleListElement.getPublished());
        }
        if(articleListElement.getOrder() != null){
            switch (articleListElement.getOrder()){
                case Order.Ascend_Create_Time: queryWrapper.orderByAsc(Article::getCreateTime);
                    break;
                case Order.Descend_Create_Time: queryWrapper.orderByDesc(Article::getCreateTime);
                    break;
                case Order.Ascend_Update_Time: queryWrapper.orderByAsc(Article::getUpdateTime);
                    break;
                case Order.Descend_Update_Time: queryWrapper.orderByDesc(Article::getUpdateTime);
                    break;
                default: return null;
            }
        }
        if(articleListElement.getTitle() != null){
            queryWrapper.like(Article::getTitle,articleListElement.getTitle());
        }
        //目录检索
        if(articleListElement.getTagId() != null){
            LambdaQueryWrapper<ArticleTag> queryWrapper1=new LambdaQueryWrapper<>();
            queryWrapper1.eq(ArticleTag::getTagId,articleListElement.getTagId());
            List<ArticleTag> articleTagList = articleTagService.list(queryWrapper1);
            List<String> articleIdList = new ArrayList<>();
            for(ArticleTag articleTag : articleTagList){
                articleIdList.add(articleTag.getArticleId());
            }
            if (articleIdList != null && !articleIdList.isEmpty()) {
                queryWrapper.in(Article::getId, articleIdList);
            }else{
                queryWrapper.eq(Article::getId, null);
            }
//            List<String> tagIdList = articleListElement.getTagIdList();
////            List<String> articleIds = articleTagService.list
//            List<Object> articleIds = articleTagService.listObjs(
//                    Wrappers.<ArticleTag>lambdaQuery()
//                            .in(ArticleTag::getTagId, tagIdList)
//                            .select(ArticleTag::getArticleId)
//            );
//            // 将List<Object>转换为List<String>
//            List<String> stringArticleIds = articleIds.stream()
//                    .map(Object::toString)
//                    .collect(Collectors.toList());
//            queryWrapper.in(Article::getId, stringArticleIds);
//            for(ArticleTag articleTag : articleTagList){
//
//
//            }
//            queryWrapper.in(Article::getId,articleTagList.getArticleId());
//            queryWrapper.select(Article.class)
//                    .leftJ
        }
        //分页
        if(articleListElement.getPageNumber() == null ||articleListElement.getPageSize()==null){
            return null;
        }
        Page<Article> page = new Page<>(articleListElement.getPageNumber(), articleListElement.getPageSize());
        IPage<Article> articleIPage = this.page(page,queryWrapper);
//        List<Article> articleList = articleIPage.getRecords();
        return articleIPage;
    }

    @Override
    public IPage<Article> getArticleList(Integer pageNumber, Integer pageSize, String userId, String tagId, String title, Integer published, Integer order) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //获取个人文章
        if(userId != null)
            queryWrapper.eq(Article::getUserId,userId);
        //是否公开
        if(published != null){
            queryWrapper.eq(Article::getPublished,published);
        }
        //排序
        if(order != null){
            switch (order){
                case Order.Ascend_Create_Time: queryWrapper.orderByAsc(Article::getCreateTime);
                    break;
                case Order.Descend_Create_Time: queryWrapper.orderByDesc(Article::getCreateTime);
                    break;
                case Order.Ascend_Update_Time: queryWrapper.orderByAsc(Article::getUpdateTime);
                    break;
                case Order.Descend_Update_Time: queryWrapper.orderByDesc(Article::getUpdateTime);
                    break;
                default: return null;
            }
        }
        //标题检索
        if(title != null){
            queryWrapper.like(Article::getTitle,title);
        }

        //目录检索
        if(tagId != null){
            LambdaQueryWrapper<ArticleTag> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(ArticleTag::getTagId,tagId);
            List<ArticleTag> articleTagList = articleTagService.list(queryWrapper1);
            for(ArticleTag articleTag : articleTagList){
                queryWrapper.eq(Article::getId,articleTag.getArticleId());
            }
        }
        //分页
        if(pageNumber == null ||pageSize==null){
            return null;
        }
        Page<Article> page = new Page<>(pageNumber, pageSize);
        IPage<Article> articleIPage = this.page(page,queryWrapper);
//        List<Article> articleList = articleIPage.getRecords();
        return articleIPage;
    }
}
