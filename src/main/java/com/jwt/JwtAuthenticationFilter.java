package com.jwt;

import com.security.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken =request.getHeader("Authorization");
        //kiem tra xem header Authorization co chua thong tin jwt khong
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            //lay jwt tu request
            String jwt = getJwtFromRequest(request);
            if(StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                //lay username tu choi jwt
                String userName= jwtTokenProvider.getUserNameFromJwt(jwt);
                //lay thong tin nguoi dung tu userID
                UserDetails userDetails= customUserDetailsService.loadUserByUsername(userName);
                if(userDetails!=null) {
                    //neu nguoi dung hop le set thong tin cho security context
                    UsernamePasswordAuthenticationToken authentication= new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                }
            }
        }
        catch(Exception e) {
            log.error("fail on set authentication",e);
        }
        filterChain.doFilter(request, response);
    }
}
