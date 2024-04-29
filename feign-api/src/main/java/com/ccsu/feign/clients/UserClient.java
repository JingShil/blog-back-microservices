package com.ccsu.feign.clients;


import com.ccsu.feign.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("user-service")
public interface UserClient {

//    @GetMapping("/user/get")
//    User findById(@RequestParam String id);

    @GetMapping("/user/api/get")
    public User apiGetUser(@RequestParam(value = "userId") String userId);

    @PostMapping("/user/api/save")
    public void apiSaveUser(@RequestBody User user);

    @GetMapping("/user/api/gets")
    public List<User> apiGetUserList(@RequestBody List<String> userIds);
}
