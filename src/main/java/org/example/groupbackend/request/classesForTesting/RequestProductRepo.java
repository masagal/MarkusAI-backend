package org.example.groupbackend.request.classesForTesting;

import org.example.groupbackend.request.RequestProduct;
import org.springframework.data.repository.ListCrudRepository;

public interface RequestProductRepo extends ListCrudRepository<RequestProduct, Long> {
}
