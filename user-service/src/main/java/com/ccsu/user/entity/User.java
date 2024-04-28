package com.ccsu.user.entity;

import lombok.Data;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.sql.Date;
import java.time.LocalDateTime;


@Data
public class User {
    private String id;
    private String accountName;
    private String phone;
    private String email;
    private String password;
    private String name;
    private Date birthday;
    private String avatar;
    private Integer sex;
    private String location;
    private String college;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate(){
        createTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onupdate(){
        updateTime=LocalDateTime.now();
    }
}
