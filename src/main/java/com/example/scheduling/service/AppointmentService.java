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
        ServiceItem serviceItem = obterEValidarServico(request.serviceItemId());
        User funcionario = userService.findById(request.funcionarioId());
        validarConflito(funcionario, request.horario());

        Appointment appointment = criarNovoAgendamento(cliente, funcionario, serviceItem, request.horario());
        Appointment saved = appointmentRepository.save(appointment);
        return toResponse(saved);
    }

    public List<AppointmentResponse> listarPorUsuario(User usuario) {
        List<Appointment> appointments = obterAgendamentosPorTipoUsuario(usuario);
        return appointments.stream().map(this::toResponse).toList();
    }

    @Transactional
    public AppointmentResponse cancelar(Long id, User solicitante) {
        Appointment appointment = obterAgendamento(id);
        validarStatusParaCancelamento(appointment);
        validarAutorizacaoCancelamento(appointment, solicitante);

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

    private ServiceItem obterEValidarServico(Long serviceItemId) {
        ServiceItem serviceItem = serviceItemService.findById(serviceItemId);
        if (!serviceItem.isAtivo()) {
            throw new BusinessException("Serviço está inativo");
        }
        return serviceItem;
    }

    private Appointment criarNovoAgendamento(User cliente, User funcionario, ServiceItem serviceItem, LocalDateTime horario) {
        return Appointment.builder()
                .serviceItem(serviceItem)
                .cliente(cliente)
                .funcionario(funcionario)
                .horario(horario)
                .status(AppointmentStatus.AGENDADO)
                .build();
    }

    private List<Appointment> obterAgendamentosPorTipoUsuario(User usuario) {
        if (usuario.getRole() == Role.CLIENTE) {
            return appointmentRepository.findByClienteId(usuario.getId());
        }
        return appointmentRepository.findByFuncionarioId(usuario.getId());
    }

    private Appointment obterAgendamento(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Agendamento não encontrado"));
    }

    private void validarStatusParaCancelamento(Appointment appointment) {
        if (appointment.getStatus() == AppointmentStatus.CONCLUIDO) {
            throw new BusinessException("Agendamento já concluído não pode ser cancelado");
        }
    }

    private void validarAutorizacaoCancelamento(Appointment appointment, User solicitante) {
        boolean autorizado = appointment.getCliente().getId().equals(solicitante.getId())
                || appointment.getFuncionario().getId().equals(solicitante.getId());
        if (!autorizado) {
            throw new BusinessException("Usuário não autorizado a cancelar este agendamento");
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
