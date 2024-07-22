package org.example.groupbackend.products;

import org.springframework.data.repository.ListCrudRepository;

public interface ProductDbRepo extends ListCrudRepository<Product, Long> {
    Product getByName(String name);
}
