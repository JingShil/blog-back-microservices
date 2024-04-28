package com.ccsu.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ccsu.article.entity.Reply;
import com.ccsu.article.mapper.ReplyMapper;
import com.ccsu.article.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReplyServiceImpl extends ServiceImpl<ReplyMapper, Reply> implements ReplyService {

//    @Autowired
//    private UserService userService;
//
//    @Value("${img.downloadPath}")
//    private String downloadPath;
//
//    @Override
//    public List<ReplyDto> getReplyList(String commentId) {
//
//        LambdaQueryWrapper<Reply> replyLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        replyLambdaQueryWrapper.eq(Reply::getCommentId,commentId);
//        replyLambdaQueryWrapper.orderByAsc(Reply::getCreateTime);
//
//        List<Reply> replyList = this.list(replyLambdaQueryWrapper);
//        List<ReplyDto> replyDtoList = new ArrayList<>();
//        // 获取所有评论的用户信息
//        List<String> userIds = replyList.stream().map(Reply::getUserId).collect(Collectors.toList());
//        if(userIds==null||userIds.size()==0){
//            return null;
//        }
//        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
////        if(userIds!=nu)
//        userLambdaQueryWrapper.in(User::getId,userIds);
//        List<User> userList = userService.list(userLambdaQueryWrapper);
//        Map<String, User> userMap = new HashMap<>();
//        for(User user : userList){
//            userMap.put(user.getId(),user);
//        }
//        //获取用户信息
//        for(Reply reply:replyList){
//            User user = userMap.get(reply.getUserId());
//            ReplyDto replyDto = new ReplyDto();
//            replyDto.setReply(reply);
//            replyDto.setUserAvatar(user.getAvatar());
//            replyDto.setUserName(user.getName());
//            replyDtoList.add(replyDto);
//        }
//
//
//        return replyDtoList;
//    }
}
