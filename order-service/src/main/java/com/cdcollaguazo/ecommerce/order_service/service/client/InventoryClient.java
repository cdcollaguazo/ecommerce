package com.cdcollaguazo.ecommerce.order_service.service.client;

import com.cdcollaguazo.ecommerce.order_service.dto.ReduceInventoryQuantityRequest;

import java.util.concurrent.CompletableFuture;

public interface InventoryClient {

    CompletableFuture<Void> reduceInventoryQuantityRequest(String sku, ReduceInventoryQuantityRequest request);

}
