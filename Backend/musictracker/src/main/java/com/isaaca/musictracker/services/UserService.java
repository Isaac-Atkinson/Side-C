package com.isaaca.musictracker.services;

import com.isaaca.musictracker.entities.User;
import com.isaaca.musictracker.exceptions.UserAlreadyExistsException;
import com.isaaca.musictracker.repositories.UserRepository;
import com.isaaca.musictracker.dto.request.LoginRequest;
import com.isaaca.musictracker.dto.request.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;


    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            SecurityContextRepository securityContextRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    public User registerUser(RegisterRequest registerRequest) {
        if(userRepository.findUserByUsername(registerRequest.username()).isPresent()){
            throw new UserAlreadyExistsException();
        }

        User user = new User();
        user.setUsername(registerRequest.username());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));

        return userRepository.save(user);
    }

    public void loginUser(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

        securityContextRepository.saveContext(context, request, response);
    }
}
