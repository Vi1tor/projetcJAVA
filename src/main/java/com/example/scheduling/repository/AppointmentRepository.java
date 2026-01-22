package com.example.scheduling.repository;

import com.example.scheduling.model.entity.Appointment;
import com.example.scheduling.model.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    boolean existsByFuncionarioAndHorario(User funcionario, LocalDateTime horario);
    List<Appointment> findByClienteId(Long clienteId);
    List<Appointment> findByFuncionarioId(Long funcionarioId);
}
