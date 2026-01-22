package com.example.scheduling.service;

import com.example.scheduling.dto.UserResponse;
import com.example.scheduling.exception.NotFoundException;
import com.example.scheduling.model.entity.User;
import com.example.scheduling.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getNome(), user.getEmail(), user.getRole());
    }
}
