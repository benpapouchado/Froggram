package backend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;


public interface UserRepository extends JpaRepository<User, UUID> {

    List<User> findByUsername(String username);
}
