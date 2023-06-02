package com.keita.riggs.jwt_config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtToken {

    private final SecurityConfig securityConfig;

    @Autowired
    public JwtToken(SecurityConfig securityConfig) {
        this.securityConfig = securityConfig;
    }

    public String getAccessToken(String username, Collection<? extends GrantedAuthority> authorities,
                                 Algorithm algorithm, HttpServletRequest request) {
        return (
                JWT
                        .create()
                        .withSubject(username)
                        .withIssuedAt(new Date(System.currentTimeMillis()))
                        .withExpiresAt(new Date(System.currentTimeMillis() + securityConfig.getAccessTokenExpirationDateInt()))
                        .withIssuer(request.getRequestURI())
                        .withClaim(
                                "roles",
                                authorities.stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .collect(Collectors.toList())
                        )
                        .sign(algorithm)
                );
    }

    public String getJwtAccessToken(HttpServletRequest request) {
        Cookie[] cookie = request.getCookies();
        if (cookie != null) {
            return Arrays.stream(cookie)
                    .filter(c -> c.getName().equals("accessToken"))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }


    public Cookie getCookie(String token, String tokenName, String username, int maxAge) {
        Cookie cookie = new Cookie(tokenName, token);
        if(tokenName.equals("refreshToken")) {
            cookie.setHttpOnly(false);
            cookie.setSecure(true);
        }else {
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
        }
        cookie.setPath("/");
        cookie.setMaxAge(securityConfig.getRefreshTokenExpirationDateInt());
        return cookie;
    }

    public String getRefreshToken(String username, Algorithm algorithm, HttpServletRequest request) {
        return (
                JWT
                        .create()
                        .withSubject(username)
                        .withIssuedAt(new Date(System.currentTimeMillis()))
                        .withExpiresAt(new Date(System.currentTimeMillis() + securityConfig.getAccessTokenExpirationDateInt()))
                        .withIssuer(request.getRequestURI())
                        .sign(algorithm)
                );
    }
}
