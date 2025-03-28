package com.nest.repository;

import com.nest.domain.Account;
import com.nest.domain.Likes;
import com.nest.domain.TargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    @Query("SELECT COUNT(l) FROM Likes l WHERE l.targetId = :targetId AND l.targetType = :targetType")
    Long countByTargetIdAndTargetType(@Param("targetId") Long targetId, @Param("targetType") TargetType targetType);

    @Query("SELECT l.account FROM Likes l WHERE l.targetId = :targetId AND l.targetType = :targetType")
    List<Account> findAccountByTargetIdAndTargetType(@Param("targetId") Long targetId, @Param("targetType") TargetType targetType);

    @Query("SELECT COUNT(l) > 0 FROM Likes l WHERE l.account.id = :accountId and targetId = :targetId and targetType=:targetType")
    Boolean existsByAccountAndTargetIdAndTargetType(Long accountId, Long targetId, TargetType targetType);

    @Query("SELECT l FROM Likes l WHERE l.account.id = :accountId and targetId = :targetId and targetType=:targetType")
    Optional<Likes> findByAccountIdAndTargetIdAndTargetType(Long accountId, Long targetId, TargetType targetType);

}
