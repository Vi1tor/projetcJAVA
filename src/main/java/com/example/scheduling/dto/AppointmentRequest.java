package com.example.scheduling.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AppointmentRequest(
        @NotNull Long serviceItemId,
        @NotNull Long funcionarioId,
        @NotNull @Future LocalDateTime horario
) {}
