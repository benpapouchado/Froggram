package backend.like;

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
@Table(name = "post_likes")
public class Like {

    @Id
    @Column(name = "like_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID likeId;

    @Column(name = "liked_by")
    private UUID likedBy;

    @Column(name = "post_id")
    private UUID postId;

    @Column(name = "liked_at")
    private LocalDateTime likedAt;

    public Like(UUID likeId, UUID likedBy,
            UUID postId, LocalDateTime likedAt) {
        this.likeId = likeId;
        this.likedBy = likedBy;
        this.postId = postId;
        this.likedAt = likedAt;
    }
}
