package backend.follow;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "follow")
public class Follow {

    @Id
    @Column(name = "follower_id")
    private UUID followerId;

    @Column(name = "followee_id")
    private UUID followeeId;

    private Follow(UUID followerId, UUID followeeId) {
        this.followerId = followerId;
        this.followeeId = followeeId;
    }

}
