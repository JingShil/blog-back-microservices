package com.ccsu.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ccsu.article.entity.ArticleTag;
import com.ccsu.article.mapper.ArticleTagMapper;
import com.ccsu.article.service.ArticleTagService;
import org.springframework.stereotype.Service;

@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {
}
