package com.cdcollaguazo.ecommerce.inventory_service.service.impl;

import com.cdcollaguazo.ecommerce.inventory_service.dto.InventoryOperationRequest;
import com.cdcollaguazo.ecommerce.inventory_service.dto.InventoryRequest;
import com.cdcollaguazo.ecommerce.inventory_service.dto.InventoryResponse;
import com.cdcollaguazo.ecommerce.inventory_service.entity.Inventory;
import com.cdcollaguazo.ecommerce.inventory_service.execption.InventoryExistsException;
import com.cdcollaguazo.ecommerce.inventory_service.execption.InventoryNotFoundException;
import com.cdcollaguazo.ecommerce.inventory_service.handler.InventoryOperationManager;
import com.cdcollaguazo.ecommerce.inventory_service.mapper.InventoryMapper;
import com.cdcollaguazo.ecommerce.inventory_service.repository.InventoryRepository;
import com.cdcollaguazo.ecommerce.inventory_service.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final Logger log = LoggerFactory.getLogger(InventoryServiceImpl.class);
    private final InventoryOperationManager inventoryOperationManager;

    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository, InventoryOperationManager inventoryOperationManager) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryOperationManager = inventoryOperationManager;
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

    @Override
    public InventoryResponse applyInventoryOperation(String sku, InventoryOperationRequest request) {
        Inventory inventory = inventoryRepository.findBySku(sku)
                .orElseThrow(() -> new InventoryNotFoundException("sku", sku));

        Inventory updatedInventory = inventoryOperationManager.apply(inventory, request);

        return InventoryMapper.toInventoryResponse(updatedInventory);
    }

}
