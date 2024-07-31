package tech.masagal.markusai.inventory.model;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import tech.masagal.markusai.products.Product;

@Repository
public interface InventoryDbRepo extends ListCrudRepository<InventoryItem, String> {
    InventoryItem findByProduct(Product product);
}
