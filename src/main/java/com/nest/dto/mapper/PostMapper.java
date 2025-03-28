package com.nest.dto.mapper;

import com.nest.domain.Post;
import com.nest.dto.PostDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(source = "account.name" , target = "accountName")
    @Mapping(source = "account.id", target = "accountId")
    PostDto toPostDto(Post post);
    List<PostDto> toPostDtoList(List<Post> postList);

    default Page<PostDto> toPostDtoPage(Page<Post> postPage) {
        return postPage.map(this::toPostDto);
    }
}
