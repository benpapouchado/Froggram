package backend.user;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "user_id", columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    // @ManyToMany(cascade = { CascadeType.ALL })
    // @JoinTable(
    // name = "track",
    // joinColumns = { @JoinColumn(name = "recording_id") },
    // inverseJoinColumns = { @JoinColumn(name = "release_id") })

    @Column(name = "username", length = 20, nullable = false)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "bio", length = 50, nullable = true)
    private String bio;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public User(UUID userId, String username, String passwordHash,
            String bio, LocalDate dateOfBirth, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.bio = bio;
        this.dateOfBirth = dateOfBirth;
    }

    public User(String username, String passwordHash,
            String bio, LocalDate dateOfBirth, LocalDateTime createdAt) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.bio = bio;
        this.dateOfBirth = dateOfBirth;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

// curl -X POST "http://localhost:8080/api/user/create" -H 'Content-Type: application/json' -d '{"username": "benpapouchado", "password_hash": "birb", "bio" : "dump him", "date_of_birth" : "1994-05-27"}'

// SELECT
//     column_name,
//     data_type,
//     is_nullable,
//     column_default
// FROM information_schema.columns
// WHERE table_name = 'photo'
// AND table_schema = 'public'
// ORDER BY ordinal_position;