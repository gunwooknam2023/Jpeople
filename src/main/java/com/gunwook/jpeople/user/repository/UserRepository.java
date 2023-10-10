package com.gunwook.jpeople.user.repository;

import com.gunwook.jpeople.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    Optional<User> findByKakaoId(String kakaoId);
}
