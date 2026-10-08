package com.cdcollaguazo.ecommerce.inventory_service.repository;

import com.cdcollaguazo.ecommerce.inventory_service.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @Transactional(readOnly = true)
    Optional<Inventory> findBySku(String sku);

    @Transactional(readOnly = true)
    boolean existsBySku(String sku);

}
