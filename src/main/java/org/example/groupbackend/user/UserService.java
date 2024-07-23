package org.example.groupbackend.user;

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

    public UserDto saveUser(UserDto userDto) {
        LOGGER.info("Creating user in Clerk: " + userDto.getEmail());

        // Create user in Clerk
        UserDto clerkUser = clerkService.createUser(userDto);

        // Save user in local database
        User user = toEntity(clerkUser);
        userRepository.save(user);

        LOGGER.info("User saved in local database with ID: " + user.getId());

        return toDto(user);
    }

    public List<UserDto> getAllUsers() {
        LOGGER.info("Fetching all users");

        List<UserDto> users = userRepository.findAll().stream()
                .map(this::toDto)
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
        UserDto userDto = toDto(user);

        LOGGER.info("User fetched with ID: " + id);

        return userDto;
    }

    private User toEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setIsAdmin(userDto.getIsAdmin());
        return user;
    }

    private UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setIsAdmin(user.getIsAdmin());
        return dto;
    }
}
