package com.nest.repository;

import com.nest.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    @Query(value = "SELECT p.* FROM Post p " +
            "WHERE p.account_id IN (SELECT f.following_id FROM Follow f WHERE f.follower_id = :followerId) " +
            "ORDER BY p.create_date DESC",
            nativeQuery = true)
    Page<Post> findPostByFollowerId(@Param("followerId") Long followerId, Pageable pageable);

    @Query(value = "SELECT p.* FROM Post p WHERE p.account_id = :accountId ORDER BY p.create_date DESC",nativeQuery = true)
    Page<Post> findByAccountId(@Param("accountId") Long accountID, Pageable pageable);
}
