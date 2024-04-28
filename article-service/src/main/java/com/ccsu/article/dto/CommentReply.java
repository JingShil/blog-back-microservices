package com.ccsu.article.dto;

import lombok.Data;

import java.util.List;

@Data
public class CommentReply {
    private CommentDto commentDto;
    private List<ReplyDto> replyDtoList;
}
