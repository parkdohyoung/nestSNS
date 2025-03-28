package com.nest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FollowDto {
    private Long followerId;
    private Long followingId;
}
