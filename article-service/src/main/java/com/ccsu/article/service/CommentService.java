package com.ccsu.article.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ccsu.article.dto.CommentReply;
import com.ccsu.article.entity.Comment;


public interface CommentService extends IService<Comment> {
    public IPage<CommentReply> getComments(String articleId,
                                           Integer pageNumber,
                                           Integer pageSize);
}
