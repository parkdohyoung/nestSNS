package com.nest.dto.mapper;

import com.nest.domain.Likes;
import com.nest.dto.LikesDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LikesMapper {
    @Mapping(source = "account.id", target = "accountId")
    @Mapping(target = "increase", ignore = true)
    LikesDto toLikesDto(Likes likes);
}
