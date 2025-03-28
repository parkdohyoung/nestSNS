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
public class Keyword extends BasicEntity {
    @Column(nullable = false)
    private String keyword;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = true)
    private Post post; // 키워드가 연결된 게시물

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = true)
    private Comment comment; // 키워드가 연결된 댓글

    public Keyword() {
    }

    public Keyword(String keyword, Post post, Comment comment) {
        this.keyword = keyword;
        this.post = post;
        this.comment = comment;
    }
}
