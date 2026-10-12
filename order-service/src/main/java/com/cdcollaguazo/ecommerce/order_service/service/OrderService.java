package com.cdcollaguazo.ecommerce.order_service.service;

import com.cdcollaguazo.ecommerce.order_service.dto.OrderRequest;
import com.cdcollaguazo.ecommerce.order_service.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request, String userId);
    List<OrderResponse> getOrders(String userId, boolean isAdmin);
    OrderResponse getOrderById(Long id);
    void deleteOrderById(Long id);

}
