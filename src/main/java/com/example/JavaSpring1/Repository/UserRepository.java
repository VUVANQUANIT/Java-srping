package com.example.JavaSpring1.Repository;

import com.example.JavaSpring1.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);  // Đúng: findByEmail với chữ E viết hoa
    boolean existsByEmail(String email);
}
