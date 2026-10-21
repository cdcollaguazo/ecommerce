package com.cdcollaguazo.ecommerce.order_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aggregateId;

    @Enumerated(EnumType.STRING)
    private EventType type;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private LocalDateTime createdAt;

    private boolean isProcessed;

}
