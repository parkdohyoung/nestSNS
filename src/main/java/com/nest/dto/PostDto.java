package com.nest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String message;
    private String imagePath;
    private Long likeCount;
    private String accountName;
    private Long accountId;
    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;

}
