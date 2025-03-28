package com.nest.controller;

import com.nest.common.util.ErrorMessages;
import com.nest.common.util.JwtUtil;
import com.nest.dto.*;
import com.nest.service.AccountService;
import com.nest.service.FollowService;
import com.nest.service.PostService;
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
    public ResponseEntity<?> createAccount(@RequestBody VerifyAccountDto verifyAccountDto) {
        try {
            accountService.createAccount(verifyAccountDto);
            return ResponseEntity.ok(Map.of("message","회원가입 성공! 이메일을 확인하세요 "));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }
    }

    @GetMapping("/edit")
    public ResponseEntity<?> editForm(HttpServletRequest request, @RequestParam Long profileId ){

        Long accountId = (Long) request.getAttribute("accountId");

        if(!accountId.equals(profileId)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error",ErrorMessages.UNAUTHORIZED_ACCESS));
        }

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


    @PostMapping("/withdraw")
    public ResponseEntity<?> withdrawAccount(@RequestBody ProfileDto profileDto, HttpServletRequest request){
        log.info("회원 탈퇴 요청, EMail : {}", profileDto.getEmail());
        Long accountId = (Long) request.getAttribute("accountId");

        if(!accountId.equals(profileDto.getId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", ErrorMessages.UNAUTHORIZED_ACCESS));
        }

        //팔로우 취소
        followService.myFollowingList(accountId)
                .forEach(follow -> {followService.unfollow(accountId,follow.getId());});
        accountService.withDrawAccount(profileDto);
        return ResponseEntity.ok(Map.of("message","감사합니다. 탈퇴가 완료 되었습니다."));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto ){
        try {
            boolean isAuthenticate = accountService.loginAuthenticate(loginDto.getEmail(), loginDto.getPassword());

            if(isAuthenticate){
                Long idByEmail = accountService.getIdByEmail(loginDto.getEmail());
                String token = jwtUtil.generateToken(idByEmail, loginDto.getEmail());
                return ResponseEntity.ok(Map.of("token",token));
            }else {
               return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error",ErrorMessages.WRONG_PASSWORD));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ErrorMessages.INTERNAL_ERROR));
        }

    }

}
