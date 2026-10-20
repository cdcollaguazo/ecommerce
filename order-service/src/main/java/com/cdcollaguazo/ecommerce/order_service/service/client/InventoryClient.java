package com.cdcollaguazo.ecommerce.order_service.service.client;

import com.cdcollaguazo.ecommerce.order_service.dto.ReduceInventoryQuantityRequest;

public interface InventoryClient {

    void reduceInventoryQuantityRequest(String sku, ReduceInventoryQuantityRequest request);

}
