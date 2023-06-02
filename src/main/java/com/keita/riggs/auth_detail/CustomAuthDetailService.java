package com.keita.riggs.auth_detail;

import com.keita.riggs.handler.ExceptHandler;
import com.keita.riggs.model.Authenticate;
import com.keita.riggs.repo.AuthenticateRepo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.keita.riggs.permission.UserRole.*;

@Service
public class CustomAuthDetailService implements UserDetailsService {

    private final AuthenticateRepo authenticateRepo;
    private final HttpServletResponse servletResponse;

    @Autowired
    public CustomAuthDetailService(AuthenticateRepo authenticateRepo, HttpServletResponse servletResponse) {
        this.authenticateRepo = authenticateRepo;
        this.servletResponse = servletResponse;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Authenticate authenticate = authenticateRepo.findByEmail(username);
        if (authenticate == null) {
            throw new ExceptHandler(HttpStatus.BAD_REQUEST, servletResponse, "You have entered an invalid username or password");
        }
        return new UserAuthDetail(authenticate, grantedAuthorities(authenticate));
    }

    private Set<SimpleGrantedAuthority> grantedAuthorities(Authenticate authenticate) {
        if (authenticate.getRole().equals("User")) {
            return USER.grantedAuthorities();
        }
        return ADMIN.grantedAuthorities();
    }
}
