package tech.masagal.markusai.user;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private Boolean isAdmin;

    private String clerkId;

    private String imageUrl;

    private String invitationToken;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setClerkId(String clerkId) {
        this.clerkId = clerkId;
    }

    public String getClerkId() {
        return clerkId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getInvitationToken() {
        return invitationToken;
    }

    public String generateInvitationToken() {
        if(invitationToken != null) {
            throw new IllegalStateException("There already exists an invitation URL for this user.");
        }
        if(clerkId != null) {
            throw new IllegalStateException("This user is already associated with a Clerk identity.");
        }
        invitationToken = String.valueOf(UUID.randomUUID());
        return invitationToken;
    }
}