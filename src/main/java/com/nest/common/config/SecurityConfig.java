package com.nest.common.config;
import com.nest.common.filter.JwtFilter;
import com.nest.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;


@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    @Autowired
    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        //swagger
                        .requestMatchers("/swagger-ui/**","/v3/api-docs").permitAll()
                        //로그인
                        .requestMatchers("/api/account/login").permitAll()
                        //회원가입
                        .requestMatchers("/api/account/join","/api/account/duplicate-email","api/account/duplicate-name", "/api/auth/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtFilter(jwtUtil), SecurityContextPersistenceFilter.class) // JWT 필터 추가
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 암호화
    }


}
