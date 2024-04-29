package com.ccsu.img;

import com.ccsu.feign.clients.ArticleClient;
import com.ccsu.feign.clients.UserClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(clients = {ArticleClient.class, UserClient.class})
@SpringBootApplication
public class ImgServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImgServiceApplication.class, args);
    }

}
