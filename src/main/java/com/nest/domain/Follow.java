package com.nest.domain;

import com.nest.common.util.BasicEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Getter
@Setter
public class Follow extends BasicEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="followerId")
    private Account follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followingId")
    private Account following;
}
