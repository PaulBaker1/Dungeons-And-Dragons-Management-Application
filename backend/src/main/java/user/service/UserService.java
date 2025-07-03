package user.service;

import user.model.User;

import java.util.Optional;

public interface UserService {

    User registerUser(User user);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}