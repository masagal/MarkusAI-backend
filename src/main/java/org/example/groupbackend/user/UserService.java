package org.example.groupbackend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ClerkService clerkService;

    @Autowired
    public UserService(UserRepository userRepository, ClerkService clerkService) {
        this.userRepository = userRepository;
        this.clerkService = clerkService;
    }

    public UserDto saveUser(UserDto userDto) {
        // Create user in Clerk
        UserDto clerkUser = clerkService.createUser(userDto);

        // Save user in local database
        User user = toEntity(clerkUser);
        userRepository.save(user);
        return toDto(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Delete user from Clerk
        clerkService.deleteUser(user.getId().toString());

        // Delete user from local database
        userRepository.deleteById(id);
    }

    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
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
