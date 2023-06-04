package com.keita.riggs.jwt_config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Component
public class JwtCustomAuthorizationFilter extends OncePerRequestFilter {

    private final SecurityConfig securityConfig;
    private final JwtToken jwtToken;

    public JwtCustomAuthorizationFilter(SecurityConfig securityConfig, JwtToken jwtToken) {
        this.securityConfig = securityConfig;
        this.jwtToken = jwtToken;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = jwtToken.getJwtFormRequest(request);
        if (StringUtils.hasText(jwt) && doesRequestHeaderExist(request) && getDecodedJWT(jwt, response) != null) {
            DecodedJWT decodedJWT = getDecodedJWT(jwt, response);
            assert decodedJWT != null;
            String username = decodedJWT.getSubject();
            String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            stream(roles)
                    .forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private DecodedJWT getDecodedJWT(String token, HttpServletResponse response) throws IOException {

        try {
            Algorithm algorithm = Algorithm.HMAC256(Base64.getDecoder().decode(securityConfig.getSecurityKey()));
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token);
        }catch (Exception exception) {
            response.setStatus(FORBIDDEN.value());

            Map<String, String> error = new HashMap<>();
            error.put("error", exception.getMessage());
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }
        return null;
    }

    private boolean doesRequestHeaderExist(HttpServletRequest request) {
        return StringUtils.hasText(request.getHeader(securityConfig.getAuthorizationHeader()));
    }
}
