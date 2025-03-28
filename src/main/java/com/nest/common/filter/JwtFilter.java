package com.nest.common.filter;

import com.nest.common.config.SecurityConfig;
import com.nest.common.util.ErrorMessages;
import com.nest.common.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        log.info("Authorization Header: {}" ,authHeader);


        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);
            try {


                Claims claims = jwtUtil.validateToken(token);
                    request.setAttribute("accountId",claims.get("accountId" ,Long.class));
                    request.setAttribute("email",claims.getSubject());

                    String email = claims.getSubject();
                    Long accountId =claims.get("accountId", Long.class);

                    UserDetails userDetails = User.builder()
                            .username(email)
                            .password("")
                            .authorities("USER")
                            .build();

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());


                    SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("SecurityContext Authentication: {}", SecurityContextHolder.getContext().getAuthentication());


            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ErrorMessages.INVALID_VERIFICATION_TOKEN);
                return;
            }
        }
        filterChain.doFilter(request,response);
    }
}
