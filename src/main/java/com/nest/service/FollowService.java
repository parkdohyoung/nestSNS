package com.nest.service;

import com.nest.domain.Account;
import com.nest.domain.Follow;
import com.nest.dto.ProfileDto;
import com.nest.dto.mapper.AccountMapper;
import com.nest.dto.mapper.FollowMapper;
import com.nest.repository.AccountRepository;
import com.nest.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FollowService {

    private final FollowRepository  followRepository;
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Autowired
    public FollowService(FollowRepository followRepository, AccountRepository accountRepository, AccountMapper accountMapper) {
        this.followRepository = followRepository;
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }


    //follow 추가
    @Transactional
    public void follow(Long followerId, Long followingId ){

        Account follower = getAccountById(followerId);
        Account following = getAccountById(followingId);

        if(followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)){
            throw new IllegalArgumentException("이미 팔로우 하였습니다.");
        }
            Follow follow = new Follow();
            follow.setFollower(follower);
            follow.setFollowing(following);
            followRepository.save(follow);
    }

    private Account getAccountById(Long followerId) {
        Account follower = accountRepository.findById(followerId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팔로워 ID입니다."));
        return follower;
    }

    @Transactional
    public void unfollow(Long followerId, Long followingId){
        Follow follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId)
                .orElseThrow(() -> new IllegalArgumentException("존재 하지 않는 팔로잉 관계입니다."));
        followRepository.delete(follow);
    }

    public List<ProfileDto> myFollowingList(Long accountId){
        return accountMapper.toProfileDtoList(followRepository.findFollowing(accountId));

    }

}
