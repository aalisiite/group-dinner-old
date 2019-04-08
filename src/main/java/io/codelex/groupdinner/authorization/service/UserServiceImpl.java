package io.codelex.groupdinner.authorization.service;

import io.codelex.groupdinner.authorization.repository.LoginRepository;
import io.codelex.groupdinner.authorization.repository.RoleRepository;
import io.codelex.groupdinner.authorization.model.LoginDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;


public class UserServiceImpl implements LoginService {
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(LoginDetails loginDetails) {
        loginDetails.setPassword(bCryptPasswordEncoder.encode(loginDetails.getPassword()));
        loginDetails.setRoles(new HashSet<>(roleRepository.findAll()) {
        });
        loginRepository.save(loginDetails);
    }

    @Override
    public LoginDetails findByUsername(String username) {
        return loginRepository.findByUsername(username);
    }
}
