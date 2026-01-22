package com.example.scheduling.dto;

import java.math.BigDecimal;

public record ServiceItemResponse(Long id, String nome, String descricao, BigDecimal preco, boolean ativo) {}
