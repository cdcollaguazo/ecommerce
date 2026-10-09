package com.cdcollaguazo.ecommerce.inventory_service.service;

import com.cdcollaguazo.ecommerce.inventory_service.dto.InventoryOperationRequest;
import com.cdcollaguazo.ecommerce.inventory_service.dto.InventoryRequest;
import com.cdcollaguazo.ecommerce.inventory_service.dto.InventoryResponse;

import java.util.List;

public interface InventoryService {

    InventoryResponse getInventoryBySku(String sku);
    InventoryResponse createInventory(InventoryRequest request);
    List<InventoryResponse> getAllInventory();
    InventoryResponse updateInventory(Long id, InventoryRequest request);
    void deleteInventory(Long id);
    void runInventoryOperation(String sku, InventoryOperationRequest request);

}
