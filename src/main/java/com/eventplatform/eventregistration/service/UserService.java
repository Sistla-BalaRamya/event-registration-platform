package com.eventplatform.eventregistration.service;

import com.eventplatform.eventregistration.dto.AuthResponse;
import com.eventplatform.eventregistration.dto.LoginRequest;
import com.eventplatform.eventregistration.dto.RegisterRequest;
import com.eventplatform.eventregistration.entity.User;
import com.eventplatform.eventregistration.enums.Role;
import com.eventplatform.eventregistration.repository.UserRepository;
import com.eventplatform.eventregistration.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        Role role;
        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        } catch (Exception e) {
            role = Role.ATTENDEE;
        }
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .isVerified(false)
                .build();
        userRepository.save(user);
        return "User registered successfully";
    }

    public AuthResponse loginUser(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );
        return new AuthResponse(token, user.getRole().name(), user.getEmail());
    }
}