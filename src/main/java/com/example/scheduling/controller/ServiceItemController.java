package com.example.scheduling.controller;

import com.example.scheduling.dto.ServiceItemRequest;
import com.example.scheduling.dto.ServiceItemResponse;
import com.example.scheduling.service.ServiceItemService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/servicos")
@RequiredArgsConstructor
public class ServiceItemController {

    private final ServiceItemService serviceItemService;

    @PreAuthorize("hasRole('FUNCIONARIO')")
    @PostMapping
    public ResponseEntity<ServiceItemResponse> criar(@RequestBody @Valid ServiceItemRequest request) {
        return ResponseEntity.ok(serviceItemService.criar(request));
    }

    @GetMapping
    public ResponseEntity<List<ServiceItemResponse>> listarAtivos() {
        return ResponseEntity.ok(serviceItemService.listarAtivos());
    }

    @PreAuthorize("hasRole('FUNCIONARIO')")
    @PutMapping("/{id}/ativo/{ativo}")
    public ResponseEntity<ServiceItemResponse> ativar(@PathVariable Long id, @PathVariable boolean ativo) {
        return ResponseEntity.ok(serviceItemService.ativar(id, ativo));
    }
}
