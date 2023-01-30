package practice.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.springsecurity.model.User;

//CRUD 함수를 JpaRepository가 들고 있다.
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByUsername(String username);
}
