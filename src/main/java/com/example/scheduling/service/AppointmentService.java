package com.example.scheduling.service;

import com.example.scheduling.dto.AppointmentRequest;
import com.example.scheduling.dto.AppointmentResponse;
import com.example.scheduling.exception.BusinessException;
import com.example.scheduling.exception.NotFoundException;
import com.example.scheduling.model.entity.Appointment;
import com.example.scheduling.model.entity.ServiceItem;
import com.example.scheduling.model.entity.User;
import com.example.scheduling.model.enums.AppointmentStatus;
import com.example.scheduling.model.enums.Role;
import com.example.scheduling.repository.AppointmentRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ServiceItemService serviceItemService;
    private final UserService userService;

    @Transactional
    public AppointmentResponse criar(AppointmentRequest request, User cliente) {
        validarHorario(request.horario());
        ServiceItem serviceItem = serviceItemService.findById(request.serviceItemId());
        if (!serviceItem.isAtivo()) {
            throw new BusinessException("Serviço está inativo");
        }
        User funcionario = userService.findById(request.funcionarioId());
        validarConflito(funcionario, request.horario());

        Appointment appointment = Appointment.builder()
                .serviceItem(serviceItem)
                .cliente(cliente)
                .funcionario(funcionario)
                .horario(request.horario())
                .status(AppointmentStatus.AGENDADO)
                .build();
        Appointment saved = appointmentRepository.save(appointment);
        return toResponse(saved);
    }

    public List<AppointmentResponse> listarPorUsuario(User usuario) {
        List<Appointment> appointments;
        if (usuario.getRole() == Role.CLIENTE) {
            appointments = appointmentRepository.findByClienteId(usuario.getId());
        } else {
            appointments = appointmentRepository.findByFuncionarioId(usuario.getId());
        }
        return appointments.stream().map(this::toResponse).toList();
    }

    @Transactional
    public AppointmentResponse cancelar(Long id, User solicitante) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Agendamento não encontrado"));

        if (appointment.getStatus() == AppointmentStatus.CONCLUIDO) {
            throw new BusinessException("Agendamento já concluído não pode ser cancelado");
        }

        boolean autorizado = appointment.getCliente().getId().equals(solicitante.getId())
                || appointment.getFuncionario().getId().equals(solicitante.getId());
        if (!autorizado) {
            throw new BusinessException("Usuário não autorizado a cancelar este agendamento");
        }

        appointment.setStatus(AppointmentStatus.CANCELADO);
        return toResponse(appointment);
    }

    private void validarHorario(LocalDateTime horario) {
        if (horario.isBefore(LocalDateTime.now())) {
            throw new BusinessException("Não é permitido agendar no passado");
        }
    }

    private void validarConflito(User funcionario, LocalDateTime horario) {
        boolean exists = appointmentRepository.existsByFuncionarioAndHorario(funcionario, horario);
        if (exists) {
            throw new BusinessException("Funcionário já possui agendamento neste horário");
        }
    }

    private AppointmentResponse toResponse(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getServiceItem().getId(),
                appointment.getServiceItem().getNome(),
                appointment.getCliente().getId(),
                appointment.getCliente().getNome(),
                appointment.getFuncionario().getId(),
                appointment.getFuncionario().getNome(),
                appointment.getHorario(),
                appointment.getStatus());
    }
}
