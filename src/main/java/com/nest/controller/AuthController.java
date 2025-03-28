package com.nest.controller;

import com.nest.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.nest.common.util.ErrorMessages.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AccountService accountService;

    @Autowired
    public AuthController(AccountService accountService){
        this.accountService =accountService;
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token){
        try {
            return accountService.verifyAccount(token) ? ResponseEntity.ok(Map.of("message","인증에 성공하였습니다.")) : ResponseEntity.badRequest().body(Map.of("error", VERIFICATION_FAIL));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", INTERNAL_ERROR));
        }
    }
}
