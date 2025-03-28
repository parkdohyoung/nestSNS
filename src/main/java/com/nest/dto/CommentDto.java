package com.nest.dto;

import com.nest.domain.Account;
import com.nest.domain.Comment;
import com.nest.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class CommentDto {
    private Long id;
    private Long postId;
    private Long accountId;
    private String accountName;
    private String commentMessage;
    private Long parentCommentId;
    private Long likeCount ;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;

}
