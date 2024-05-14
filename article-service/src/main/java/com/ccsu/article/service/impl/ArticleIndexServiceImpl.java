package com.ccsu.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ccsu.article.entity.Article;
import com.ccsu.article.entity.ArticleListElement;
import com.ccsu.article.entity.ArticleTag;
import com.ccsu.article.index.ArticleIndex;
import com.ccsu.article.utils.canstant.Order;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleIndexServiceImpl {

    @Autowired
    private RestHighLevelClient client;

    /**
     * 文档
     */
    private final String index = "article_index";

    /**
     * 保存或更新数据
     * @param article
     */
    public void saveOrUpdate(Article article){
        ArticleIndex articleIndex = new ArticleIndex();
        articleIndex.setArticle(article);
        IndexRequest request;
        if(article.getId()!=null) {
            request = new IndexRequest(index)
                    .id(article.getId())
                    .source(JSON.toJSONString(articleIndex), XContentType.JSON);
        }
        else{
            request = new IndexRequest(index)
                    .source(JSON.toJSONString(articleIndex), XContentType.JSON);
        }
        try {
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 根据id删除数据
     * @param id
     */
    public void delete(String id){
        DeleteRequest request = new DeleteRequest(index,id);
        try {
            DeleteResponse response = client.delete(request,RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据id获取文档
     * @param id
     */
    public Article getById(String id){
        GetRequest request = new GetRequest(index,id);
        GetResponse response = null;
        try {
            response = client.get(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String json = response.getSourceAsString();
        ArticleIndex articleIndex = JSONToArticleIndex(json);
        if(articleIndex==null){
            return null;
        }
        Article article = new Article();
        article.setArticleIndex(articleIndex);
        return article;
    }

    public IPage<Article> search(ArticleListElement articleListElement){

        if(articleListElement==null){
            return null;
        }

        SearchRequest request = new SearchRequest(index);
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if(articleListElement.getUserId() != null){
            boolQueryBuilder.must(QueryBuilders.termQuery("userId",articleListElement.getUserId()));
        }
        if(articleListElement.getPublished() != null){
            boolQueryBuilder.must(QueryBuilders.termQuery("published",articleListElement.getPublished()));
        }
        if(articleListElement.getOrder() != null){
            switch (articleListElement.getOrder()){
                case Order.Ascend_Create_Time: request.source().sort("createTime", SortOrder.ASC);
                    break;
                case Order.Descend_Create_Time: request.source().sort("createTime", SortOrder.DESC);
                    break;
                case Order.Ascend_Update_Time: request.source().sort("updateTime", SortOrder.ASC);
                    break;
                case Order.Descend_Update_Time: request.source().sort("updateTime", SortOrder.DESC);
                    break;
//                default: return null;
            }
        }
        if(articleListElement.getTitle() != null){
//            request.source().query(QueryBuilders.multiMatchQuery(articleListElement.getTitle(),
//                    "content","title"));
            boolQueryBuilder.must(QueryBuilders.multiMatchQuery(articleListElement.getTitle(),
                    "content","title"));
        }
        //目录检索


        //分页
        if(articleListElement.getPageNumber() != null ||articleListElement.getPageSize()!=null){
            int pageNumber = articleListElement.getPageNumber();
            int pageSize = articleListElement.getPageSize();
            request.source().from((pageNumber-1)*pageSize).size(pageSize);
        }

        request.source().query(boolQueryBuilder);

        try {
            SearchResponse response = client.search(request,RequestOptions.DEFAULT);
            SearchHits responseHits = response.getHits();
            SearchHit[] hits = responseHits.getHits();
            List<Article> articleList = new ArrayList<>();
            for(SearchHit searchHit:hits){
                ArticleIndex articleIndex = JSONToArticleIndex(searchHit.getSourceAsString());
                Article article = new Article();
                article.setArticleIndex(articleIndex);
                articleList.add(article);
            }
            IPage<Article> articleIPage = new Page<>();
            articleIPage.setTotal(responseHits.getTotalHits().value);
            articleIPage.setRecords(articleList);
            return articleIPage;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        request.source().query(QueryBuilders.multiMatchQuery());
    }



    /**
     * 将response的json数据转为ArticleIndex
     * @param json
     * @return
     */
    private ArticleIndex JSONToArticleIndex(String json){
        ArticleIndex articleIndex = JSON.parseObject(json, ArticleIndex.class);
        return articleIndex;
    }

}
