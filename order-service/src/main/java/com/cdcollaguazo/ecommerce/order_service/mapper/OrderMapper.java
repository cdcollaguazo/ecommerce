package com.cdcollaguazo.ecommerce.order_service.mapper;

import com.cdcollaguazo.ecommerce.order_service.dto.OrderLineItemRequest;
import com.cdcollaguazo.ecommerce.order_service.dto.OrderLineItemResponse;
import com.cdcollaguazo.ecommerce.order_service.dto.OrderRequest;
import com.cdcollaguazo.ecommerce.order_service.dto.OrderResponse;
import com.cdcollaguazo.ecommerce.order_service.entity.Order;
import com.cdcollaguazo.ecommerce.order_service.entity.OrderLineItem;

import java.util.List;

public class OrderMapper {

    public static OrderLineItem toOrderLineItem(OrderLineItemRequest request, Order order) {
        return OrderLineItem.builder()
                .sku(request.sku())
                .price(request.price())
                .quantity(request.quantity())
                .order(order)
                .build();
    }

    public static Order toOrder(OrderRequest request) {
        Order order = new Order();

        List<OrderLineItem> items = request.orderLineItemRequests().stream()
                .map(item -> toOrderLineItem(item, order))
                .toList();

        order.setOrderLineItems(items);
        return order;
    }

    public static OrderLineItemResponse toOrderLineItemResponse(OrderLineItem item) {
        return new OrderLineItemResponse(item.getId(), item.getSku(), item.getPrice(), item.getQuantity());
    }

    public static OrderResponse toOrderResponse(Order order) {
        List<OrderLineItemResponse> orderLineItemResponses = order.getOrderLineItems().stream()
                .map(OrderMapper::toOrderLineItemResponse)
                .toList();

        return new OrderResponse(order.getId(), order.getOrderNumber(), orderLineItemResponses);
    }

}
