package com.nest.controller;

import com.nest.common.util.ErrorMessages;
import com.nest.common.util.JwtUtil;
import com.nest.domain.Account;
import com.nest.dto.*;
import com.nest.service.AccountService;
import com.nest.service.FollowService;
import com.nest.service.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/account")
@Tag( name ="회원관련 API" , description = "회원가입, 수정 조회등 API 명세서 ")
public class AccountController {

    private final AccountService accountService;
    private final FollowService followService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AccountController(AccountService accountService, FollowService followService, JwtUtil jwtUtil){
        this.accountService =accountService;
        this.jwtUtil = jwtUtil;
        this.followService = followService;

    }

    @GetMapping("/duplicate-email")
    public ResponseEntity<Map<String,Boolean>> duplicateEmail(@RequestParam String email){
        boolean duplicate = accountService.isDuplicateEmail(email);
        return ResponseEntity.ok(Map.of("exists", duplicate));
    }
    @GetMapping("/duplicate-name")
    public ResponseEntity<Map<String,Boolean>> duplicateName(@RequestParam String name){
        boolean duplicate = accountService.isDuplicateEmail(name);
        return ResponseEntity.ok(Map.of("exists", duplicate));
    }
    @PostMapping("/join")
    public ResponseEntity<?> createAccount(@RequestBody JoinDto joinDto) {
        try {
            accountService.createAccount(joinDto);
            return ResponseEntity.ok(Map.of("message","회원가입 성공! 이메일을 확인하세요 "));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }
    }

    @GetMapping("/edit")
    public ResponseEntity<?> editForm(HttpServletRequest request){

        Long accountId = (Long) request.getAttribute("accountId");

        return ResponseEntity.ok(accountService.getProfileById(accountId));
    }

    @PostMapping("/edit")
    public ResponseEntity<?> edit(HttpServletRequest request,
                                  @RequestBody ProfileDto profileDto){
        Long accountId = (Long) request.getAttribute("accountId");

        if(!accountId.equals(profileDto.getId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error",ErrorMessages.UNAUTHORIZED_ACCESS));
        }

        return ResponseEntity.ok(accountService.updateAccount(profileDto));
    }

    @PostMapping("/edit/profileImage")
    public ResponseEntity<?> profilImage(@RequestParam Long accountId, HttpServletRequest request , @RequestParam(value = "file") MultipartFile file){
        Long accountIdByToken = (Long) request.getAttribute("accountId");
        if(!accountId.equals(accountIdByToken)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error",ErrorMessages.UNAUTHORIZED_ACCESS));
        }
        return ResponseEntity.ok(accountService.updateProfileImage(accountId, file));

    }


    @DeleteMapping("/withdraw")
    public ResponseEntity<?> withdrawAccount(@RequestBody HttpServletRequest request){
        Long accountId = (Long) request.getAttribute("accountId");

        //팔로우 취소
        followService.myFollowingList(accountId)
                .forEach(follow -> {followService.unfollow(accountId,follow.getId());});
        accountService.withDrawAccount(accountId);
        return ResponseEntity.ok(Map.of("message","감사합니다. 탈퇴가 완료 되었습니다."));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto ){
        accountService.loginAuthenticate(loginDto.getEmail(), loginDto.getPassword());
        Account accountByEmail = accountService.getAccountByEmail(loginDto.getEmail());
        String token = jwtUtil.generateToken(accountByEmail.getId(), loginDto.getEmail(), accountByEmail.getName());
        return ResponseEntity.ok(Map.of("token",token));
    }

}
