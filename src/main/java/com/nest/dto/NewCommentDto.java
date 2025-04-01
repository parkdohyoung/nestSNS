package com.nest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewCommentDto {
    private Long postId;
    private String commentMessage;
    private Long parentCommentId;
}
