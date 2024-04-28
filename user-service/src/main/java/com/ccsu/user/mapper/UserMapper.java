package com.ccsu.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ccsu.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
