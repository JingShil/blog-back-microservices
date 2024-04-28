package com.ccsu.feign.clients;


import com.ccsu.feign.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("user-service")
public interface UserClient {

    @GetMapping("/user/get")
    User findById(@RequestParam String id);
}
