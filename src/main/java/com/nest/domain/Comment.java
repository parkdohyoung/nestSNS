package com.nest.domain;

import com.nest.common.util.BasicEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Comment extends BasicEntity implements Likeable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false)
    private String commentMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parentComment_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies = new ArrayList<>();

    private Long likeCount;



    @Override
    public void incrementLikeCount(){
        if(this.likeCount == null ){
            this.likeCount = 0L;
        }
        this.likeCount++;
    }
    @Override
    public void decrementLikeCount() {
        if (this.likeCount == null || this.likeCount <= 0) {
            this.likeCount = 0L;
        } else {
            this.likeCount--;
        }
    }

}
