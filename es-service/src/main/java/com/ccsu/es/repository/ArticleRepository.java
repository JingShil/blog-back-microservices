package com.ccsu.es.repository;


import com.ccsu.article.index.ArticleIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends ElasticsearchRepository<ArticleIndex, String> {


}
