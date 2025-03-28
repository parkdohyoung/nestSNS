package com.nest.repository;

import com.nest.domain.Post;
import com.nest.domain.SavedPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SavedPostRepository extends JpaRepository<SavedPost,Long> {


    @Query(value = "SELECT p.* FROM Post p " +
            "WHERE p.id IN (SELECT s.post_id FROM saved_post s WHERE s.account_id = :accountId) " +
            "ORDER BY p.create_date DESC", nativeQuery = true)
    Page<Post> findByAccountId(@Param("accountId") Long accountID, Pageable pageable);

    @Query("Select s from SavedPost s where s.account.id =:accountId and s.post.id =:postId ")
    Optional<SavedPost> findByAccountIdAndPostId(@Param("accountId") Long accountId, @Param("postId") Long postId);

    @Query("SELECT COUNT(s) > 0 FROM SavedPost s WHERE s.account.id = :accountId and s.post.id = :postId")
    Boolean existsByAccountAndPost(Long accountId, Long postId) ;


}
