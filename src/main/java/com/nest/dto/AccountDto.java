package com.nest.dto;


import com.nest.domain.Account;
import com.nest.domain.AccountStatus;
import com.nest.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class AccountDto {
    private Long id ;
    private String email;
    private String password;
    private String name;
    private String profileImgPath;
    private String profileMessage;
    private String verificationToken;
    private boolean verified =false ;
    private LocalDateTime loginDateTime;
    private List<Post> post;
    private AccountStatus status ;
}
