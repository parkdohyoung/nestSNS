package com.nest.repository;

import com.nest.domain.Comment;
import com.nest.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    public List<Comment> findByPostId(Long postId);
    public List<Comment> findByParentCommentId(Long commentId);
    public List<Comment> findByPostIdAndParentCommentIsNull(Long postId);
}

