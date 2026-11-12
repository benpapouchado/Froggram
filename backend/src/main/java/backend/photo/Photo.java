package backend.photo;

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
@Table(name = "photo")
public class Photo {

    @Id
    @Column(name = "photo_id", columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID photoId;

    @Column(name = "photo_url", nullable = false)
    private String photoUrl;

    @Column(name = "thumbnail_url", nullable = false)
    private String thumbnailUrl;

    @Column(name = "caption", length = 50, nullable = true)
    private String caption;

    @Column(name = "likeCount")
    private int likeCount;

    @Column(name = "posted_by")
    private UUID postedBy;

    private Photo(UUID photoId, String photoUrl, String caption,
            int likeCount, UUID postedBy) {
        this.photoId = photoId;
        this.photoUrl = photoUrl;
        this.caption = caption;
        this.likeCount = likeCount;
        this.postedBy = postedBy;
    }

}
