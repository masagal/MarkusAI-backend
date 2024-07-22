package org.example.groupbackend.request.classesForTesting;

import org.springframework.data.repository.ListCrudRepository;

public interface UserRepoTest extends ListCrudRepository<UserTestClass, Long> {
}
