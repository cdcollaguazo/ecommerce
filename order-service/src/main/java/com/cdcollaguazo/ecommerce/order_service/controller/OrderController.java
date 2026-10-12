package com.cdcollaguazo.ecommerce.order_service.controller;

import com.cdcollaguazo.ecommerce.order_service.dto.OrderRequest;
import com.cdcollaguazo.ecommerce.order_service.dto.OrderResponse;
import com.cdcollaguazo.ecommerce.order_service.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid OrderRequest request,
                                                     JwtAuthenticationToken authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request, authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(JwtAuthenticationToken authentication) {
        String userId = authentication.getName();

        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equalsIgnoreCase);

        return ResponseEntity.ok(orderService.getOrders(userId, isAdmin));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        orderService.deleteOrderById(id);

        return ResponseEntity.noContent().build();
    }

}
