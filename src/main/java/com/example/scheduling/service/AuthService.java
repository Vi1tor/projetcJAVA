package com.example.scheduling.service;

import com.example.scheduling.dto.AuthRequest;
import com.example.scheduling.dto.AuthResponse;
import com.example.scheduling.dto.RegisterUserRequest;
import com.example.scheduling.dto.UserResponse;
import com.example.scheduling.exception.BusinessException;
import com.example.scheduling.model.entity.User;
import com.example.scheduling.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Value("${security.jwt.expiration-seconds:3600}")
    private long expirationSeconds;

    public UserResponse register(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email já cadastrado");
        }
        User user = User.builder()
                .nome(request.nome())
                .email(request.email())
                .senha(passwordEncoder.encode(request.senha()))
                .role(request.role())
                .criadoEm(LocalDateTime.now())
                .build();
        User saved = userRepository.save(user);
        return new UserResponse(saved.getId(), saved.getNome(), saved.getEmail(), saved.getRole());
    }

    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha()));

        UserDetails user = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        return new AuthResponse(token, "Bearer", expirationSeconds);
    }
}
