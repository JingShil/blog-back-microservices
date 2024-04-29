package com.ccsu.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ccsu.user.dto.Result;
import com.ccsu.user.dto.UserInfo;
import com.ccsu.user.entity.User;
import com.ccsu.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Value("${user.adminId}")
    private String adminId;


    @PostMapping("/login")
    public Result<User> login(@RequestBody User user){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone,user.getPhone());


        User dataUser = userService.getOne(queryWrapper);
        if(dataUser == null){
            return Result.error("账号未注册");
        }
        if(!dataUser.getPassword().equals(user.getPassword()) ){
            return Result.error("密码错误");
        }

        String token = setTokenAndToRedis(dataUser.getId());

        return Result.successByToken(dataUser,token);
    }

    public String setTokenAndToRedis(String id){
        UUID uuid = UUID.randomUUID();
        String token = uuid.toString();
        redisTemplate.opsForValue().set("blog:token:" + id,token,18000L, TimeUnit.SECONDS);
        return token;
    }

    @GetMapping("/info")
    public Result<User> getUserInfo(@RequestParam String userId){
        User user = userService.getById(userId);
        return Result.success(user);
    }

    @GetMapping("/article/info")
    public Result<UserInfo> getArticleUserInfo(@RequestParam String userId){
        User user = userService.getById(userId);
        UserInfo userInfo = userToUserInfo(user);
        return Result.success(userInfo);
    }

    @GetMapping("/admin/info")
    public Result<UserInfo> getAdminInfo(){
        User user = userService.getById(adminId);
        UserInfo userInfo = userToUserInfo(user);
        return Result.success(userInfo);
    }

    private UserInfo userToUserInfo(User user){
        UserInfo userInfo = new UserInfo();
        userInfo.setAvatar(user.getAvatar());
        userInfo.setBirthday(user.getBirthday());
        userInfo.setCollege(user.getCollege());
        userInfo.setName(user.getName());
        userInfo.setSex(user.getSex());
        userInfo.setUpdateTime(user.getUpdateTime());
        userInfo.setCreateTime(user.getCreateTime());
        userInfo.setLocation(user.getLocation());
        return userInfo;
    }

    @GetMapping("/api/get")
    public User apiGetUser(@RequestParam(value = "userId") String userId){
        User user = userService.getById(userId);
        return user;
    }

    @PostMapping("/api/save")
    public void apiSaveUser(@RequestBody User user){
        userService.saveOrUpdate(user);
    }

    @GetMapping("/api/gets")
    public List<User> apiGetUserList(@RequestBody List<String> userIds){
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.in(User::getId,userIds);
        List<User> userList = userService.list(userLambdaQueryWrapper);
        return userList;
    }

    @PostMapping("/save")
    public Result<String> save(@RequestHeader("UserId") String userId,@RequestBody User user){
//        String id=tokenUtil.extractSubjectFromToken(userId);
        user.setId(userId);
        userService.saveOrUpdate(user);
        return Result.success("保存成功");
    }
}
