package com.example.scheduling.repository;

import com.example.scheduling.model.entity.ServiceItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceItemRepository extends JpaRepository<ServiceItem, Long> {
    List<ServiceItem> findByAtivoTrue();
}
