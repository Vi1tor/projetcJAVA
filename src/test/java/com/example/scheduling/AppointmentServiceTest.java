package com.example.scheduling;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.example.scheduling.dto.AppointmentRequest;
import com.example.scheduling.exception.BusinessException;
import com.example.scheduling.model.entity.ServiceItem;
import com.example.scheduling.model.entity.User;
import com.example.scheduling.model.enums.Role;
import com.example.scheduling.repository.AppointmentRepository;
import com.example.scheduling.service.AppointmentService;
import com.example.scheduling.service.ServiceItemService;
import com.example.scheduling.service.UserService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private ServiceItemService serviceItemService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AppointmentService appointmentService;

    private User cliente;
    private User funcionario;
    private ServiceItem serviceItem;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        cliente = User.builder().id(1L).nome("Cliente").role(Role.CLIENTE).build();
        funcionario = User.builder().id(2L).nome("Funcionario").role(Role.FUNCIONARIO).build();
        serviceItem = ServiceItem.builder().id(3L).nome("Corte").descricao("desc").preco(BigDecimal.TEN).ativo(true).build();
    }

    @Test
    void deveRejeitarAgendamentoNoPassado() {
        AppointmentRequest request = new AppointmentRequest(serviceItem.getId(), funcionario.getId(), LocalDateTime.now().minusDays(1));
        when(serviceItemService.findById(serviceItem.getId())).thenReturn(serviceItem);
        when(userService.findById(funcionario.getId())).thenReturn(funcionario);
        assertThrows(BusinessException.class, () -> appointmentService.criar(request, cliente));
    }
}
