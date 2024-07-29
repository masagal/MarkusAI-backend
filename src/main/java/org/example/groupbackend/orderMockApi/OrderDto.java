package org.example.groupbackend.orderMockApi;

import java.time.LocalDateTime;

public record OrderDto(OrderStatus status, LocalDateTime approvedDate, Long requestId) {
}
