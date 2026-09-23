package com.isaaca.musictracker.services;

import com.isaaca.musictracker.entities.User;
import com.isaaca.musictracker.exceptions.EmailAlreadyInUseException;
import com.isaaca.musictracker.exceptions.UserAlreadyExistsException;
import com.isaaca.musictracker.repositories.UserRepository;
import com.isaaca.musictracker.requests.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

        if(userRepository.existsByEmail(registerRequest.getEmail())){
            throw new EmailAlreadyInUseException();
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        return userRepository.save(user);
    }
}
