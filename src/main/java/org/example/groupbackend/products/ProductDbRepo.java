package org.example.groupbackend.products;

import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface ProductDbRepo extends ListCrudRepository<Product, Long> {
    Optional<Product> getByName(String name);
}
