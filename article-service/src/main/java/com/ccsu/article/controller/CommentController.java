package com.ccsu.article.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ccsu.article.dto.CommentReply;
import com.ccsu.article.dto.Result;
import com.ccsu.article.entity.Comment;
import com.ccsu.article.entity.Reply;
import com.ccsu.article.service.CommentService;
import com.ccsu.article.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/article/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private ReplyService replyService;



    @GetMapping("/get")
    public Result<IPage<CommentReply>> getComments(@RequestParam(value = "articleId") String articleId,
                                                   @RequestParam(value = "pageNumber") Integer pageNumber,
                                                   @RequestParam(value = "pageSize") Integer pageSize){
        if(articleId==null||pageNumber==null||pageSize==null){
            return Result.error("错误");
        }

        return Result.success(commentService.getComments(articleId,pageNumber,pageSize));

    }


    @DeleteMapping("/delete/reply")
    public Result<String> deleteReply(@RequestParam String replyId){
        if(replyId==null){
            return Result.error("错误");
        }
        replyService.removeById(replyId);
        return Result.success("成功");
    }

    @DeleteMapping("/delete/comment")
    public Result<String> deleteComment(@RequestParam String commentId){
        if(commentId==null){
            return Result.error("错误");
        }
        LambdaQueryWrapper<Reply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Reply::getCommentId,commentId);
        List<Reply> replyList = replyService.list(queryWrapper);
        replyService.removeBatchByIds(replyList);
        commentService.removeById(commentId);
        return Result.success("成功");
    }

    @PostMapping("/add/comment")
    public Result<String> addComment(@RequestBody Comment comment){
        if(comment==null){
            return Result.error("错误");
        }
        String id = UUID.randomUUID().toString();
        comment.setId(id);
        commentService.saveOrUpdate(comment);
        return Result.success("成功");
    }

    @PostMapping("/add/reply")
    public Result<String> addReply(@RequestBody Reply reply){
        if(reply==null){
            return Result.error("错误");
        }
        String id = UUID.randomUUID().toString();
        reply.setId(id);
        replyService.saveOrUpdate(reply);
        return Result.success("成功");
    }
}
