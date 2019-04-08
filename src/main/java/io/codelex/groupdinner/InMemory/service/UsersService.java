package io.codelex.groupdinner.InMemory.service;

import io.codelex.groupdinner.api.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsersService {

    private List<User> users = new ArrayList<>();

    public UsersService() {
    }

    public User addUser (User user) {
        users.add(user);
        return user;
    }

    public Optional<User> getUser(Long id) {
        return users.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }
    
}
