package com.nest.repository;

import com.nest.domain.Account;
import com.nest.domain.Follow;
import com.nest.domain.TargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Long> {
    @Query("Select f.follower from Follow f where f.following.id = :accountId")
    List<Account>findFollowers(@Param("accountId") long accountId);

    @Query("Select f.following from Follow f where f.follower.id = :accountId")
    List<Account>findFollowing(@Param("accountId") long accountId);

    @Query("SELECT f FROM Follow f WHERE f.follower.id = :followerId AND f.following.id = :followingId")
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    @Query("SELECT COUNT(f) > 0 FROM Follow f where f.follower.id = :followerId AND f.following.id=:followingId")
    Boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
