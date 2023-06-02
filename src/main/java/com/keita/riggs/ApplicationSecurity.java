package com.keita.riggs;

import com.keita.riggs.jwt_config.CustomAuthenticationFilter;
import com.keita.riggs.jwt_config.JwtCustomAuthorizationFilter;
import com.keita.riggs.jwt_config.JwtToken;
import com.keita.riggs.jwt_config.SecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class ApplicationSecurity {

    private final AuthenticationProvider authenticationProvider;
    private final SecurityConfig securityConfig;
    private final JwtToken jwtToken;
    private final JwtCustomAuthorizationFilter jwtCustomAuthorizationFilter;
    private final AuthenticationManager authenticationManager;

    private static final String[] PUBLIC_ACCESS = {
            "/riggs/user/**"
    };

    private static final String[] PRIVATE_ACCESS = {
            "/riggs/room/**"
    };

//    public ApplicationSecurity(AuthenticationProvider authenticationProvider, SecurityConfig securityConfig, JwtToken jwtToken, JwtCustomAuthorizationFilter jwtCustomAuthorizationFilter) {
//        this.authenticationProvider = authenticationProvider;
//        this.securityConfig = securityConfig;
//        this.jwtToken = jwtToken;
//        this.jwtCustomAuthorizationFilter = jwtCustomAuthorizationFilter;
//    }


    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthorizationFilter = new CustomAuthenticationFilter(securityConfig , jwtToken, authenticationManager);
        customAuthorizationFilter.setFilterProcessesUrl("/riggs/login");
        return http.csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers(
                       PUBLIC_ACCESS
                ).permitAll()
                .requestMatchers(PRIVATE_ACCESS).hasAnyAuthority("USER_ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtCustomAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }
}
