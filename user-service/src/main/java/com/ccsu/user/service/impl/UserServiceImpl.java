package com.ccsu.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ccsu.user.entity.User;
import com.ccsu.user.mapper.UserMapper;
import com.ccsu.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
