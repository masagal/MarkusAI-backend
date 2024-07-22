package org.example.groupbackend.inventory.model;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryDbRepo extends ListCrudRepository<InventoryItem, String> {

}
