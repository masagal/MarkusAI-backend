package tech.masagal.markusai.request;

import tech.masagal.markusai.user.User;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface RequestRepository extends ListCrudRepository<Request, Long> {
    List<Request> findAllByUser(User user);
}
