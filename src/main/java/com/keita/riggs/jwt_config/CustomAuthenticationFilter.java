package com.keita.riggs.jwt_config;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keita.riggs.auth_detail.UserAuthDetail;
import com.keita.riggs.exception.CustomCredentialInputCheck;
import com.keita.riggs.model.Authenticate;
import io.jsonwebtoken.io.Decoders;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@Slf4j
public class CustomAuthenticationFilter  extends UsernamePasswordAuthenticationFilter {

    private final SecurityConfig securityConfig;
    private final JwtToken jwtToken;
    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(SecurityConfig securityConfig, JwtToken jwtToken, AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.securityConfig = securityConfig;
        this.jwtToken = jwtToken;
        this.authenticationManager = authenticationManager;
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            Authenticate authenticationInput = new ObjectMapper()
                    .readValue(request.getInputStream(), Authenticate.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(authenticationInput.getEmail(), authenticationInput.getPassword());
            if (authenticationInput.getEmail().isEmpty() || authenticationInput.getPassword().isEmpty()) {
                throw new CustomCredentialInputCheck(authenticationInput, HttpStatus.UNAUTHORIZED, response);
            }
            return authenticationManager.authenticate(authentication);
        }catch (IOException e) {
            throw new CustomCredentialInputCheck("Invalid username or password", HttpStatus.UNAUTHORIZED, response);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        throw new CustomCredentialInputCheck("Invalid username or password", HttpStatus.UNAUTHORIZED, response);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        UserAuthDetail user = (UserAuthDetail) authResult.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256(encodeSecurityKey());
        String accessToken = jwtToken.getAccessToken(user.getUsername(), user.getAuthorities(), algorithm, request);
        String refreshToken = jwtToken.getRefreshToken(user.getUsername(), algorithm, request);

        Map<String, String> token = new HashMap<>();
        token.put("accessToken", accessToken);
        token.put("email", user.getUsername());
        user.getAuthorities()
                .forEach(object -> token.put(object.toString(), object.toString()));
        token.put("refreshToken", refreshToken);

        Cookie cookie = jwtToken.getCookie(accessToken, "accessToken");
        response.addCookie(cookie);
        cookie = jwtToken.getCookie(refreshToken, "refreshToken");
        response.addCookie(cookie);

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper()
                .writeValue(response.getOutputStream(), token);
    }

    private byte[] encodeSecurityKey() {
        return Decoders.BASE64.decode(securityConfig.getSecurityKey());
    }

}
