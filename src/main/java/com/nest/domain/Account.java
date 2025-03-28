package com.nest.domain;

import com.nest.common.util.BasicEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
public class Account extends BasicEntity {
    @Column(nullable = false)
    private String email;

    @Setter
    @Column(nullable = false)
    private String password;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    private String profileMessage;

    @Setter
    private String profileImgPath;

    @Setter
    private String verificationToken;

    @Setter
    private boolean verified = false;

    public Account(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
