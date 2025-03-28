package com.nest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class JwtResponse {
    private String token;

    public JwtResponse(String token) {
        this.token = token;
    }

}
