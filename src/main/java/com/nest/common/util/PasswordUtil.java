package com.nest.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {

    private final PasswordEncoder passwordEncorder;

    @Autowired
    public PasswordUtil(PasswordEncoder passwordEncoder){
        this.passwordEncorder = passwordEncoder;
    }

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String encode(String rawPassword){
        return passwordEncoder.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String encodedPassword){
        return passwordEncoder.matches(rawPassword,encodedPassword);
    }

}
