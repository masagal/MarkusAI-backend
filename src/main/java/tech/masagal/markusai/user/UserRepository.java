package tech.masagal.markusai.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByClerkId(String clerkId);
    User findByInvitationToken(String invitationToken);
}
