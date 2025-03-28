package com.nest.dto.mapper;

import com.nest.domain.Account;
import com.nest.domain.Follow;
import com.nest.dto.FollowDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FollowMapper {
    @Mapping(source = "follower.id", target = "followerId")
    @Mapping(source = "following.id", target = "followingId")
    FollowDto toFollowDto(Follow follow);}
