package com.ccsu.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ccsu.article.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
