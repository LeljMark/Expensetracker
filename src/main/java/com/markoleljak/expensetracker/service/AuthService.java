package com.markoleljak.expensetracker.service;

import com.markoleljak.expensetracker.dto.LoginResponse;
import com.markoleljak.expensetracker.dto.LoginRequest;
import com.markoleljak.expensetracker.dto.RegisterRequest;
import com.markoleljak.expensetracker.exception.EmailAlreadyUsedException;
import com.markoleljak.expensetracker.model.User;
import com.markoleljak.expensetracker.repository.UserRepository;
import com.markoleljak.expensetracker.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;

    }

    public void register(RegisterRequest request) {
        log.info("Registration attempt for email={}", request.email());

        if (userRepository.findByEmail(request.email()).isPresent()) {
            log.warn("Registration failed: email already in use email={}", request.email());
            throw new EmailAlreadyUsedException("Email already registered: " + request.email());
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setCreatedAt(Instant.now());
        userRepository.save(user);

        log.info("User registered successfully email={}", request.email());
    }

    public LoginResponse login(LoginRequest request) {
        log.info("Login attempt for email={}", request.email());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch (Exception ex) {
            log.warn("Login failed for email={}", request.email());
            throw ex;
        }

        String jwt = jwtUtil.generateToken(request.email());

        log.info("Login successful for email={}", request.email());

        return new LoginResponse(jwt, request.email());
    }
}
