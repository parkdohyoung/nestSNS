package com.nest.dto;

import com.nest.domain.TargetType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class NotificationDto {
    private Long id;
    private String content ;
    private Long senderId;
    private Long receiverId;
    private TargetType targetType;
    private Long targetId; // 게시글 또는 댓글의 ID
    private boolean checked ;
    private LocalDateTime createDate;
}
