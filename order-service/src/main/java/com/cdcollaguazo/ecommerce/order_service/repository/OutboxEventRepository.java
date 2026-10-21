package com.cdcollaguazo.ecommerce.order_service.repository;

import com.cdcollaguazo.ecommerce.order_service.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {

    @Transactional(readOnly = true)
    List<OutboxEvent> findByIsProcessedFalse();

}
