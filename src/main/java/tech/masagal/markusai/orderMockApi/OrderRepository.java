package tech.masagal.markusai.orderMockApi;

import org.springframework.data.repository.ListCrudRepository;

public interface OrderRepository extends ListCrudRepository<Order, Long> {
}
