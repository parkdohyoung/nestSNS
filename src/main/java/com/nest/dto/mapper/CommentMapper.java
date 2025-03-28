package com.nest.dto.mapper;

import com.nest.domain.Comment;
import com.nest.dto.CommentDto;
import com.nest.dto.EditCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
        @Mapping(source = "account.name" , target = "accountName")
        @Mapping(source = "account.id", target = "accountId")
        @Mapping(source = "post.id" , target = "postId")
        @Mapping(source = "parentComment.id", target ="parentCommentId")
        CommentDto toCommentDto(Comment comment);
        List<CommentDto> toCommentDtoList (List<Comment> commentList);

        @Mapping(source = "account.name" , target = "accountName")
        @Mapping(source = "account.id", target = "accountId")
        @Mapping(source = "post.id" , target = "postId")
        @Mapping(source = "parentComment.id", target ="parentCommentId")
        EditCommentDto toEditCommentDto(Comment comment);

}