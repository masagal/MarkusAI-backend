package org.example.groupbackend.request;

import org.example.groupbackend.request.classesForTesting.ProductTest;

public record RequestDto(int quantity, ProductTest prod, Long userId) {
}
