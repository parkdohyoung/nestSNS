package com.nest.dto;

import com.nest.domain.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {
    private Long id ;
    private String name;
    private String email;
    private String profileImgPath;
    private String profileMessage;
    private AccountStatus status ;

}
