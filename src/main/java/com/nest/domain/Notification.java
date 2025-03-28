package com.nest.domain;

import com.nest.common.util.BasicEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Notification extends BasicEntity {

    @Column(nullable = false)
    private String content ;

    @ManyToOne
    @JoinColumn(name = "sender_id" , nullable = false)
    private Account Sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id" , nullable = false)
    private Account receiver;

    @Column(nullable = false)
    private TargetType targetType;

    @Column(nullable = false)
    private Long targetId; // 게시글 또는 댓글의 ID


    @Column(nullable = false)
    private boolean checked =false;


}
