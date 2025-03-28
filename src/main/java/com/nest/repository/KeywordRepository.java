package com.nest.repository;

import com.nest.domain.Comment;
import com.nest.domain.Keyword;
import com.nest.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword,Long> {
    void deleteByPost(Post post);
    void deleteByComment(Comment comment);
    @Query(value = "SELECT k.post FROM Keyword k WHERE k.keyword = :keyword ORDER BY k.createDate DESC")
    Page<Post> findPostByKeyword(@Param("keyword")String keyword, Pageable pageable);
}
