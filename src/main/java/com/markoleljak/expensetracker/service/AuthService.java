package com.markoleljak.expensetracker.service;

import com.markoleljak.expensetracker.dto.AuthResponse;
import com.markoleljak.expensetracker.dto.LoginRequest;
import com.markoleljak.expensetracker.dto.RegisterRequest;
import com.markoleljak.expensetracker.exception.EmailAlreadyUsedException;
import com.markoleljak.expensetracker.model.User;
import com.markoleljak.expensetracker.repository.UserRepository;
import com.markoleljak.expensetracker.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

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
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new EmailAlreadyUsedException("Email already registered: " + request.email());
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setCreatedAt(Instant.now());
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        String jwt = jwtUtil.generateToken(request.email());

        return new AuthResponse(jwt, request.email());
    }
}
