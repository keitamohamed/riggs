package com.keita.riggs.jwt_config;

import com.google.common.net.HttpHeaders;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class SecurityConfig {

    private String securityKey;
    private String tokenPrefix;
    private long accessTokenExpirationDate;
    private long refreshTokenExpirationDate;

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }

    public Date accessTokenExpireDate() {
        Date date = new Date();
        return new Date(date.getTime() + accessTokenExpirationDate);
    }

    public Date refreshTokenExpireDate() {
        Date date = new Date();
        return new Date(date.getTime() + refreshTokenExpirationDate);
    }
}
