package org.example.groupbackend.user;

public record UserDto(Long id, String clerkId, String name, String email, boolean isAdmin) {
    static public UserDto fromUser(User user) {
        return new UserDto(user.getId(), user.getClerkId(), user.getName(), user.getEmail(), user.getIsAdmin());
    }
}
