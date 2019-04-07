package io.codelex.groupdinner.authorization.service;

import io.codelex.groupdinner.authorization.repository.LoginRepository;
import io.codelex.groupdinner.authorization.model.LoginDetails;
import io.codelex.groupdinner.authorization.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private LoginRepository loginRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginDetails loginDetails = loginRepository.findByUsername(username);
        if(loginDetails == null) throw new UsernameNotFoundException(username);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : loginDetails.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
       return new org.springframework.security.core.userdetails.User(loginDetails.getUsername(), loginDetails.getPassword(), grantedAuthorities);
    }
}
