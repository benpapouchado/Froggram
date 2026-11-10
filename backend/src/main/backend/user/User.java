package backend.user;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "user_id", columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "track",
            joinColumns = { @JoinColumn(name = "recording_id") },
            inverseJoinColumns = { @JoinColumn(name = "release_id") })    

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

    private User(UUID userId, String username, String passwordHash,
            String bio, LocalDate dateOfBirth, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.bio = bio;
        this.dateOfBirth = dateOfBirth;
    }

}
