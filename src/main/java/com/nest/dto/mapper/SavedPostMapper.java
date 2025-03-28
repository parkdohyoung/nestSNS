package com.nest.dto.mapper;

import com.nest.domain.SavedPost;
import com.nest.dto.SavedPostDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SavedPostMapper {
    @Mapping(source = "account.id" , target = "accountId")
    @Mapping(source = "post.id", target = "postId")
    SavedPostDto toSavePostDto(SavedPost savedPost);

}
