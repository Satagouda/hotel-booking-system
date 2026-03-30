package com.community.authservice.service;

import com.community.authservice.dto.AuthRequest;
import com.community.authservice.entity.User;
import com.community.authservice.exception.InvalidCredentialsException;
import com.community.authservice.exception.UserAlreadyExistsException;
import com.community.authservice.exception.UserNotFoundException;
import com.community.authservice.repo.UserRepository;
import com.community.authservice.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public String register(AuthRequest request) {

        repository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new UserAlreadyExistsException("User already exists with email: " + request.getEmail());
        });
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        repository.save(user);
        return "User registered";
    }

    public String login(AuthRequest request) {
        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }

        return jwtUtil.generateToken(user.getEmail());
    }
}