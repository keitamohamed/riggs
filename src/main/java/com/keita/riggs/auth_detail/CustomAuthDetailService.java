package com.keita.riggs.auth_detail;

import com.keita.riggs.exception.NotFoundException;
import com.keita.riggs.model.Authenticate;
import com.keita.riggs.repo.AuthenticateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.keita.riggs.permission.UserRole.ADMIN;
import static com.keita.riggs.permission.UserRole.USER;

@Service
public class CustomAuthDetailService implements UserDetailsService {

    private final AuthenticateRepo authenticateRepo;

    @Autowired
    public CustomAuthDetailService(AuthenticateRepo authenticateRepo) {
        this.authenticateRepo = authenticateRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Authenticate authenticate = authenticateRepo.findByEmail(username);
        if (authenticate == null) {
            throw new NotFoundException("You have entered an invalid username or password");
        }
        return new UserAuthDetail(authenticate, grantedAuthorities(authenticate));
    }

    private Set<SimpleGrantedAuthority> grantedAuthorities(Authenticate authenticate) {
        if (authenticate.getRole().equals("USER")) {
            return USER.grantedAuthorities();
        }
        return ADMIN.grantedAuthorities();
    }
}
