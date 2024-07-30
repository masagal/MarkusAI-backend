package tech.masagal.markusai.user;

public record UserDto(Long id, String clerkId, String name, String email, boolean isAdmin, String imageUrl) {
    static public UserDto fromUser(User user) {
        return new UserDto(user.getId(), user.getClerkId(), user.getName(), user.getEmail(), user.getIsAdmin(), user.getImageUrl());
    }
}