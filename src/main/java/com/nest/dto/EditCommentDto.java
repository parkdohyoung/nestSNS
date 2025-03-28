package com.nest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class EditCommentDto {
    private Long id;
    private Long postId;
    private Long accountId;
    private String accountName;
    private String commentMessage;
    private Long parentCommentId;
}
