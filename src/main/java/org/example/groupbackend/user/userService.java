package org.example.groupbackend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class userService {

    private final userRepository userRepository;
    private final ClerkService clerkService;

    @Autowired
    public userService(userRepository userRepository, ClerkService clerkService) {
        this.userRepository = userRepository;
        this.clerkService = clerkService;
    }

    public userDto saveUser(userDto userDto) {
        // Create user in Clerk
        userDto clerkUser = clerkService.createUser(userDto);

        // Save user in local database
        User user = toEntity(clerkUser);
        userRepository.save(user);
        return toDto(user);
    }

    public List<userDto> getAllUsers() {
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

    public userDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private User toEntity(userDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setIsAdmin(userDto.getIsAdmin());
        return user;
    }

    private userDto toDto(User user) {
        userDto dto = new userDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setIsAdmin(user.getIsAdmin());
        return dto;
    }
}
