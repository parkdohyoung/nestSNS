package com.nest.domain;
import com.nest.common.util.BasicEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Likes extends BasicEntity {

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "account_id", nullable = false)
        private Account account;

        @Column(nullable = false)
        private Long targetId; // 게시글 또는 댓글의 ID

        @Column(nullable = false)
        private TargetType targetType; // "POST" 또는 "COMMENT" 구분값


}
