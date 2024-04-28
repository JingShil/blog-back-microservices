package com.ccsu.article.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ccsu.article.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
}
