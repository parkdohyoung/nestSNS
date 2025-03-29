package com.nest.domain;

import com.nest.common.util.BasicEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@NoArgsConstructor
@Entity
@Getter @Setter
public class Account extends BasicEntity {
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private String profileMessage;

    private String profileImgPath;

    private String verificationToken;

    private boolean verified = false;

    private AccountStatus status = AccountStatus.INACTIVE;

        public Account(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
