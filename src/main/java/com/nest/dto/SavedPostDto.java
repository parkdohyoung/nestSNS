package com.nest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SavedPostDto {
    private Long postId;
    private Long accountId;
}
