package com.example.scheduling.service;

import com.example.scheduling.dto.ServiceItemRequest;
import com.example.scheduling.dto.ServiceItemResponse;
import com.example.scheduling.exception.NotFoundException;
import com.example.scheduling.model.entity.ServiceItem;
import com.example.scheduling.repository.ServiceItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ServiceItemService {

    private final ServiceItemRepository repository;

    @Transactional
    public ServiceItemResponse criar(ServiceItemRequest request) {
        ServiceItem serviceItem = ServiceItem.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .preco(request.preco())
                .ativo(request.ativo() == null || request.ativo())
                .build();
        ServiceItem saved = repository.save(serviceItem);
        return toResponse(saved);
    }

    public List<ServiceItemResponse> listarAtivos() {
        return repository.findByAtivoTrue().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ServiceItemResponse ativar(Long id, boolean ativo) {
        ServiceItem serviceItem = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Serviço não encontrado"));
        serviceItem.setAtivo(ativo);
        return toResponse(serviceItem);
    }

    public ServiceItem findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Serviço não encontrado"));
    }

    private ServiceItemResponse toResponse(ServiceItem serviceItem) {
        return new ServiceItemResponse(serviceItem.getId(), serviceItem.getNome(), serviceItem.getDescricao(),
                serviceItem.getPreco(), serviceItem.isAtivo());
    }
}
