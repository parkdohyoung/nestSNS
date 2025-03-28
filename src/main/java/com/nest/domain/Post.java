package com.nest.domain;

import com.nest.common.util.BasicEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post extends BasicEntity implements Likeable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private String message;

    private String imagePath;


    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments;

    private Long likeCount;

    public boolean isBy(Long accountId){
        return this.account != null && this.account.getId().equals(accountId);
    }

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
