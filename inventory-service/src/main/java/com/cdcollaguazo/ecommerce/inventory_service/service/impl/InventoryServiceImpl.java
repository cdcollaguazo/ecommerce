package com.cdcollaguazo.ecommerce.inventory_service.service.impl;

import com.cdcollaguazo.ecommerce.inventory_service.dto.InventoryRequest;
import com.cdcollaguazo.ecommerce.inventory_service.dto.InventoryResponse;
import com.cdcollaguazo.ecommerce.inventory_service.entity.Inventory;
import com.cdcollaguazo.ecommerce.inventory_service.execption.InventoryExistsException;
import com.cdcollaguazo.ecommerce.inventory_service.execption.InventoryNotFoundException;
import com.cdcollaguazo.ecommerce.inventory_service.mapper.InventoryMapper;
import com.cdcollaguazo.ecommerce.inventory_service.repository.InventoryRepository;
import com.cdcollaguazo.ecommerce.inventory_service.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final Logger log = LoggerFactory.getLogger(InventoryServiceImpl.class);

    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public InventoryResponse getInventoryBySku(String sku) {
        return inventoryRepository.findBySku(sku)
                .map(InventoryMapper::toInventoryResponse)
                .orElseThrow(() -> new InventoryNotFoundException("sku", sku));
    }

    @Override
    public InventoryResponse createInventory(InventoryRequest request) {
        if (inventoryRepository.existsBySku(request.sku())) {
            throw new InventoryExistsException("sku", request.sku());
        }

        Inventory savedInventory = inventoryRepository.save(InventoryMapper.toInventory(request));
        log.info("Inventory created {}", savedInventory);

        return InventoryMapper.toInventoryResponse(savedInventory);
    }

    @Override
    public List<InventoryResponse> getAllInventory() {
        return inventoryRepository.findAll().stream()
                .map(InventoryMapper::toInventoryResponse)
                .toList();
    }

    @Override
    public InventoryResponse updateInventory(Long id, InventoryRequest request) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new InventoryNotFoundException("id", id));

        inventory.setSku(request.sku());
        inventory.setQuantity(request.quantity());

        Inventory updatedInventory = inventoryRepository.save(inventory);
        log.info("Inventory with id {} updated", inventory.getId());

        return InventoryMapper.toInventoryResponse(updatedInventory);
    }

    @Override
    public void deleteInventory(Long id) {
        if (!inventoryRepository.existsById(id)) {
            throw new InventoryNotFoundException("id", id);
        }

        inventoryRepository.deleteById(id);
        log.info("Inventory with id {} deleted", id);
    }

}
