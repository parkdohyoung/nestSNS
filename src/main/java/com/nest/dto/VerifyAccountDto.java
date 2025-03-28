package com.nest.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@Builder
public class VerifyAccountDto {

    private Long id ;
    private String email;
    private String name;
    private String password;
    private String verificationToken;

}
