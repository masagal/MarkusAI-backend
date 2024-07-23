package org.example.groupbackend.request;

import org.springframework.data.repository.ListCrudRepository;

public interface RequestRepository extends ListCrudRepository<Request, Long> {
}
