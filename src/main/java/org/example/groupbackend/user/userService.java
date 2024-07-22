package org.example.groupbackend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class userService {

    private final userRepository userRepository;

    @Autowired
    public userService(userRepository userRepository) {
        this.userRepository = userRepository;
    }

    public userDto saveUser(userDto userDto) {
        User user = toEntity(userDto);
        userRepository.save(user);
        return toDto(user);
    }

    public List<userDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void deleteUser(Long id) {
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
