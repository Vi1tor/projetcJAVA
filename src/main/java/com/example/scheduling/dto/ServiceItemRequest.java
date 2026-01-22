package com.example.scheduling.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ServiceItemRequest(
        @NotBlank String nome,
        @NotBlank String descricao,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal preco,
        Boolean ativo
) {}
