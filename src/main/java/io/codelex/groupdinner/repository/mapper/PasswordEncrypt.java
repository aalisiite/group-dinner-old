package io.codelex.groupdinner.repository.mapper;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PasswordEncrypt {

    public String hashPassword(String plainPassword){
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public Boolean passwordMatches(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
