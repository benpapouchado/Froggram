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
public class Comment {

    @Id
    @Column(name = "comment_id", columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID commentId;

    @Column(name = "post_id")
    private UUID postId;

    @Column(name = "content", length = 50, nullable = false)
    private String content;

    @Column(name = "posted_by")
    private UUID postedBy;

    @Column(name = "posted_at")
    private LocalDateTime posteddAt;

    private Comment(UUID commentId, String content, UUID postedBy,
            UUID postId, LocalDateTime posteddAt) {
        this.commentId = commentId;
        this.postId = postId;
        this.content = content;
        this.postedBy = postedBy;
        this.posteddAt = posteddAt;
    }

}
