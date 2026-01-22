package com.example.scheduling.dto;

import com.example.scheduling.model.enums.AppointmentStatus;
import java.time.LocalDateTime;

public record AppointmentResponse(
        Long id,
        Long serviceItemId,
        String serviceItemNome,
        Long clienteId,
        String clienteNome,
        Long funcionarioId,
        String funcionarioNome,
        LocalDateTime horario,
        AppointmentStatus status
) {}
