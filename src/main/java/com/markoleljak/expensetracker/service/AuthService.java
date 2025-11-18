package com.markoleljak.expensetracker.service;

import com.markoleljak.expensetracker.dto.LoginRequest;
import com.markoleljak.expensetracker.dto.RegisterRequest;
import com.markoleljak.expensetracker.model.User;
import com.markoleljak.expensetracker.repository.UserRepository;
import com.markoleljak.expensetracker.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;

    }

    public boolean register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            return false;
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setCreatedAt(Instant.now());

        userRepository.save(user);

        return true;
    }

    public String login(LoginRequest request) {
        return null;

        // TODO
    }
}
