package tech.masagal.markusai.inventory.model;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryDbRepo extends ListCrudRepository<InventoryItem, String> {

}
