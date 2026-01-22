package com.example.scheduling.dto;

import com.example.scheduling.model.enums.Role;

public record UserResponse(Long id, String nome, String email, Role role) {}
