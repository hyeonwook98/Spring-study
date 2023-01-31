package pratice.jwtserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pratice.jwtserver.model.User;

public interface UserRepository extends JpaRepository<User,Long> {
    public User findByUsername(String username);
}
