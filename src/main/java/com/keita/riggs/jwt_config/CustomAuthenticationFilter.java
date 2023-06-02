package com.keita.riggs.jwt_config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keita.riggs.auth_detail.UserAuthDetail;
import com.keita.riggs.handler.CustomCredentialInputCheck;
import com.keita.riggs.model.Authenticate;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@Slf4j
public class CustomAuthenticationFilter  extends UsernamePasswordAuthenticationFilter {

    private final SecurityConfig securityConfig;
    private final JwtToken jwtToken;
    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(SecurityConfig securityConfig, JwtToken jwtToken, @Lazy AuthenticationManager authenticationManager) {
        this.securityConfig = securityConfig;
        this.jwtToken = jwtToken;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            Authenticate authenticationInput = new ObjectMapper()
                    .readValue(request.getInputStream(), Authenticate.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(authenticationInput.getEmail(), authenticationInput.getPassword());
            if (authenticationInput.getEmail().isEmpty() || authenticationInput.getPassword().isEmpty()) {
                throw new CustomCredentialInputCheck(authenticationInput, HttpStatus.NOT_ACCEPTABLE, response);
            }
            return authenticationManager.authenticate(authentication);
        }catch (IOException e) {
            throw new CustomCredentialInputCheck("You have entered an invalid username or password", HttpStatus.BAD_REQUEST, response);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        throw new CustomCredentialInputCheck("You have entered an invalid username or password", HttpStatus.BAD_REQUEST, response);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserAuthDetail user = (UserAuthDetail) authResult.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256(securityConfig.getSecurityKey().getBytes());
        String accessToken = jwtToken.getAccessToken(user.getUsername(), user.getAuthorities(), algorithm, request);
        String refreshToken = jwtToken.getRefreshToken(user.getUsername(), algorithm, request);

        Map<String, String> token = new HashMap<>();
        token.put("accessToken", accessToken);
        token.put("email", user.getUsername());
        user.getAuthorities()
                .forEach(object -> token.put(object.toString(), object.toString()));
        token.put("refreshToken", refreshToken);

        JWTVerifier verifier = JWT.require(algorithm).build();

        int expireAt = (int)verifier.verify(accessToken).getExpiresAt().getTime();
        Cookie cookie = jwtToken.getCookie(accessToken, "accessToken", user.getUsername(), expireAt);
        response.addCookie(cookie);
        cookie = jwtToken.getCookie(refreshToken, "refreshToken", user.getUsername(), securityConfig.getRefreshTokenExpirationDateInt());
        response.addCookie(cookie);

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper()
                .writeValue(response.getOutputStream(), token);
    }
}
