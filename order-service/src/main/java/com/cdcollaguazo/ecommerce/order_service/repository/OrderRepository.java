package com.cdcollaguazo.ecommerce.order_service.repository;

import com.cdcollaguazo.ecommerce.order_service.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    @Transactional(readOnly = true)
    List<Order> findByUserId(String userId);

}
