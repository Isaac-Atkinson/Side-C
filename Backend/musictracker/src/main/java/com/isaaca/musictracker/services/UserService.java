package com.isaaca.musictracker.services;

import com.isaaca.musictracker.entities.User;
import com.isaaca.musictracker.exceptions.UserAlreadyExistsException;
import com.isaaca.musictracker.repositories.UserRepository;
import com.isaaca.musictracker.requests.RegisterRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(RegisterRequest registerRequest) {
        if(userRepository.findUserByUsername(registerRequest.getUsername()).isPresent()){
            throw new UserAlreadyExistsException();
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        return userRepository.save(user);
    }
}
