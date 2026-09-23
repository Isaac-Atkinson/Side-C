package com.isaaca.musictracker.controllers;

import com.isaaca.musictracker.requests.LoginRequest;
import com.isaaca.musictracker.requests.RegisterRequest;
import com.isaaca.musictracker.security.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173",
allowCredentials = "true")
@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody RegisterRequest registerRequest){
        userService.registerUser(registerRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> loginUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request){
        userService.loginUser(loginRequest, request);

        return ResponseEntity.ok().build();
    }
}
