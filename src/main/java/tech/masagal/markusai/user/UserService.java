package tech.masagal.markusai.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    private final UserRepository userRepository;
    private final ClerkService clerkService;

    @Autowired
    public UserService(UserRepository userRepository, ClerkService clerkService) {
        this.userRepository = userRepository;
        this.clerkService = clerkService;
    }

    public User saveUser(User commissioner, User newUser) {
        LOGGER.info("Commissioner creating new user" + commissioner.getEmail() + " " + newUser.getEmail());

        newUser.generateInvitationToken();
        userRepository.save(newUser);

        LOGGER.info("User saved in local database with ID: " + newUser.getId());

        return newUser;
    }

    public List<UserDto> getAllUsers() {
        LOGGER.info("Fetching all users");

        List<UserDto> users = userRepository.findAll().stream()
                .map(UserDto::fromUser)
                .collect(Collectors.toList());

        LOGGER.info("Total users fetched: " + users.size());

        return users;
    }

    public void deleteUser(Long id) {
        LOGGER.info("Deleting user with ID: " + id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LOGGER.info("User found in local database: " + user.getEmail());

        // Delete user from Clerk
        clerkService.deleteUser(user.getId().toString());

        // Delete user from local database
        userRepository.deleteById(id);

        LOGGER.info("User deleted with ID: " + id);
    }

    public UserDto getUserById(Long id) {
        LOGGER.info("Fetching user with ID: " + id);

        User user = userRepository.findById(id).orElseThrow();
        LOGGER.info("user is"+ user);
        UserDto userDto = UserDto.fromUser(user);

        LOGGER.info("User fetched with ID: " + id);

        return userDto;
    }

    public User associateUser(String clerkId, String token) {
        User user = userRepository.findByInvitationToken(token);
        if(user == null) {
            LOGGER.warning("Received an invitation token that did not match to a user.");
            throw new IllegalArgumentException("No user found to have this invitation token.");
        }
        if(user.getClerkId() != null) {
            LOGGER.warning("Invitation token has already been used.");
            throw new IllegalArgumentException("This invitation token has already been used.");
        }

        user.setClerkId(clerkId);
        userRepository.save(user);
        return user;
    }
}
