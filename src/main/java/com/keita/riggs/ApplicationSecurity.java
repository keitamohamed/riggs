package com.keita.riggs;

import com.keita.riggs.config.CustomAuthenticationFilter;
import com.keita.riggs.config.JwtCustomAuthorizationFilter;
import com.keita.riggs.config.JwtToken;
import com.keita.riggs.config.JwtSecurityToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ApplicationSecurity {

    private final AuthenticationProvider authenticationProvider;
    private final JwtSecurityToken securityConfig;
    private final JwtToken jwtToken;
    private final JwtCustomAuthorizationFilter jwtCustomAuthorizationFilter;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    public ApplicationSecurity(AuthenticationProvider authenticationProvider,
                               JwtSecurityToken securityConfig, JwtToken jwtToken,
                               JwtCustomAuthorizationFilter jwtCustomAuthorizationFilter,
                               AuthenticationConfiguration authenticationConfiguration) {
        this.authenticationProvider = authenticationProvider;
        this.securityConfig = securityConfig;
        this.jwtToken = jwtToken;
        this.jwtCustomAuthorizationFilter = jwtCustomAuthorizationFilter;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    private static final String[] PUBLIC_ACCESS = {
            "/",
            "/*.*",
            "/favicon.ico",
            "/static/favicon.ico",
            "/static/*.html",
            "/static/**",
            "/assets/*.*",
            "/riggs/user/add",
            "/riggs/room/download-image/*",
            "/riggs/room/list",
    };

    private static final String[] PRIVATE_ACCESS = {
            "/riggs/room/find-by-id/**",
            "/riggs/user/**",
            "/riggs/booking/add",
            "/riggs/booking/find-by-id/**",
            "/riggs/booking/list-of-booking",
    };
    private static final String[] ADMIN_ACCESS = {
            "/riggs/room/**",
            "/riggs/booking/**",
            "/riggs/admin/**",
            "/riggs/admin/**",
            "/riggs/admin/**/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        return http.csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(
                       PUBLIC_ACCESS
                ).permitAll()
                .requestMatchers(PRIVATE_ACCESS).hasAnyRole("USER", "ADMIN")
                .requestMatchers(ADMIN_ACCESS).hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilter(customAuthenticationFilter())
                .addFilterBefore(jwtCustomAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    public CustomAuthenticationFilter customAuthenticationFilter () throws Exception {
        CustomAuthenticationFilter authenticationFilter = new CustomAuthenticationFilter(securityConfig , jwtToken, authenticationConfiguration.getAuthenticationManager());
        authenticationFilter.setFilterProcessesUrl("/riggs/login");
        return authenticationFilter;
    }
}
