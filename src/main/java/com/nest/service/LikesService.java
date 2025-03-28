package com.nest.service;

import com.nest.common.util.ErrorMessages;
import com.nest.domain.Account;
import com.nest.domain.Likeable;
import com.nest.domain.Likes;
import com.nest.domain.TargetType;
import com.nest.dto.LikesDto;
import com.nest.repository.AccountRepository;
import com.nest.repository.CommentRepository;
import com.nest.repository.LikesRepository;
import com.nest.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Slf4j
@Service
public class LikesService {
    private final LikesRepository likeRepository;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public LikesService(LikesRepository likeRepository , AccountRepository accountRepository, PostRepository postRepository, CommentRepository commentRepository) {
        this.likeRepository = likeRepository;
        this.accountRepository = accountRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }
    @Transactional
    public void adjustLike(LikesDto likesDto){

        if(likesDto.isIncrease()){
            addLike(likesDto.getAccountId(), likesDto.getTargetId(), likesDto.getTargetType());
        }
        else {
            removeLike(likesDto.getAccountId(), likesDto.getTargetId(), likesDto.getTargetType());
        }

         Object target = switch (likesDto.getTargetType()){
            case POST -> postRepository.findById(likesDto.getTargetId()).orElseThrow(()-> new IllegalArgumentException(ErrorMessages.POST_NOT_FOUND));
            case COMMENT -> commentRepository.findById(likesDto.getTargetId()).orElseThrow(()-> new IllegalArgumentException(ErrorMessages.COMMENT_NOT_FOUND));
            default -> throw new IllegalArgumentException("잘못된 타입입니다.");
        };

        if(target instanceof Likeable likeable){
            if(likesDto.isIncrease()){
                likeable.incrementLikeCount();
            }else {
                likeable.decrementLikeCount();
            }
        }

    }



    @Transactional
    private void addLike(Long accountId, Long targetId, TargetType targetType){
        Boolean exists = likeRepository.existsByAccountAndTargetIdAndTargetType(accountId,targetId,targetType);
        if(exists){
            throw new IllegalArgumentException("이미 좋아요 하였습니다.");
        }
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Likes likes = new Likes();
        likes.setAccount(account);
        likes.setTargetId(targetId);
        likes.setTargetType(targetType);
        likeRepository.save(likes);

    }
    @Transactional
    private void removeLike(Long accountId, Long targetId, TargetType targetType) {
        Likes likes = likeRepository.findByAccountIdAndTargetIdAndTargetType(accountId, targetId, targetType)
                .orElseThrow(() -> new IllegalArgumentException("좋아요 이력이 존재하지 않습니다."));
        likeRepository.delete(likes);
    }

    public Long getLikeCount(Long targetId, TargetType targetType){
        return likeRepository.countByTargetIdAndTargetType(targetId,targetType);
    }

    public List<Account> getLikeAccount(Long targetId, TargetType targetType){
        return likeRepository.findAccountByTargetIdAndTargetType(targetId,targetType);
    }

}
