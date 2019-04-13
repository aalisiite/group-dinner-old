package io.codelex.groupdinner.authorization;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import static java.util.Collections.singleton;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Component
class AuthService {

    void authorise(String email, String password, Role role) {
        var authorities = singleton(new SimpleGrantedAuthority(role.name()));
        var token = new UsernamePasswordAuthenticationToken(email, password, authorities);
        getContext().setAuthentication(token);
    }

    void clearAuthentication() {
        getContext().setAuthentication(null);
    }
}
