package com.nest.controller;

import com.nest.common.util.ErrorMessages;
import com.nest.domain.Account;
import com.nest.dto.FollowDto;
import com.nest.dto.ProfileDto;
import com.nest.service.FollowService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/follow")
public class FollowController {

    private final FollowService followService;

    @Autowired
    public FollowController(FollowService followService) {

        this.followService = followService;
    }
    @GetMapping
    public ResponseEntity<?> myFollowingAccountList (HttpServletRequest request){
            Long accountId = (Long) request.getAttribute("accountId");
            List<ProfileDto> profileDtos = followService.myFollowingList(accountId);
            return ResponseEntity.ok(profileDtos);
    }

    @PostMapping("/")
    public ResponseEntity<?> followAdd(@RequestParam Long followingId, HttpServletRequest request ){
        Long accountId = (Long) request.getAttribute("accountId");
        followService.follow(accountId,followingId);
        return ResponseEntity.ok(Map.of("message","팔로우하였습니다."));
    }

    @PostMapping("/unfollow/{followingId}")
    public ResponseEntity<?> unfollow(@RequestParam Long followingId, HttpServletRequest request){
        Long accountId = (Long) request.getAttribute("accountId");
        followService.unfollow(accountId,followingId);
        return ResponseEntity.ok(Map.of("message","언팔로우하였습니다."));
    }




}
