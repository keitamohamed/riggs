package com.keita.riggs.jwt_config;

import com.google.common.net.HttpHeaders;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "jwt")
@Component
@Getter
@Setter
public class SecurityConfig {

    private String securityKey;
    private String tokenPrefix;
    private int accessTokenExpirationDateInt;
    private int refreshTokenExpirationDateInt;

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }
}
