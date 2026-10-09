package com.cdcollaguazo.ecommerce.order_service.service.client;

import com.cdcollaguazo.ecommerce.order_service.dto.InventoryOperationRequest;

public interface InventoryClient {

    void runInventoryOperation(String sku, InventoryOperationRequest request);

}
