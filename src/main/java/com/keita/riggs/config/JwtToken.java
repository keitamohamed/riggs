package com.keita.riggs.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtToken {

    private final JwtSecurityToken securityConfig;

    @Autowired
    public JwtToken(JwtSecurityToken securityConfig) {
        this.securityConfig = securityConfig;
    }

    public String getAccessToken(String username, Collection<? extends GrantedAuthority> authorities,
                                 Algorithm algorithm, HttpServletRequest request) {
        return (
                JWT
                        .create()
                        .withSubject(username)
                        .withIssuedAt(new Date(System.currentTimeMillis()))
                        .withExpiresAt(securityConfig.accessTokenExpireDate())
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

    public Cookie getCookie(String token, String tokenName) {
        Cookie cookie = new Cookie(tokenName, token);
        if(tokenName.equals("refreshToken")) {
            cookie.setHttpOnly(false);
            cookie.setSecure(true);
        }else {
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
        }
        cookie.setPath("/");
        cookie.setMaxAge(cookie.getMaxAge());
        return cookie;
    }

    public String getRefreshToken(String username, Algorithm algorithm, HttpServletRequest request) {
        return (
                JWT
                        .create()
                        .withSubject(username)
                        .withIssuedAt(new Date(System.currentTimeMillis()))
                        .withExpiresAt(securityConfig.refreshTokenExpireDate())
                        .withIssuer(request.getRequestURI())
                        .sign(algorithm)
                );
    }

    public String getJwtFormRequest(HttpServletRequest request) {
        String authorization = request.getHeader(securityConfig.getAuthorizationHeader());
        if (StringUtils.hasText(authorization) && authorization.startsWith(securityConfig.getTokenPrefix() + " ")) {
            return (authorization.replace(securityConfig.getTokenPrefix(),"").replaceAll("\\s+", ""));
        }
        return null;
    }
}
