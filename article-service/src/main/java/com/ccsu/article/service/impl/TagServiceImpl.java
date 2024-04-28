package com.ccsu.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ccsu.article.entity.Tag;
import com.ccsu.article.mapper.TagMapper;
import com.ccsu.article.service.TagService;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
}
