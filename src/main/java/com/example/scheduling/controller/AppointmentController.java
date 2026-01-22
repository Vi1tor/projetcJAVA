package com.example.scheduling.controller;

import com.example.scheduling.dto.AppointmentRequest;
import com.example.scheduling.dto.AppointmentResponse;
import com.example.scheduling.model.entity.User;
import com.example.scheduling.service.AppointmentService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentResponse> criar(@RequestBody @Valid AppointmentRequest request,
                                                     @AuthenticationPrincipal User cliente) {
        return ResponseEntity.ok(appointmentService.criar(request, cliente));
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> listar(@AuthenticationPrincipal User usuario) {
        return ResponseEntity.ok(appointmentService.listarPorUsuario(usuario));
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<AppointmentResponse> cancelar(@PathVariable Long id,
                                                         @AuthenticationPrincipal User usuario) {
        return ResponseEntity.ok(appointmentService.cancelar(id, usuario));
    }
}
