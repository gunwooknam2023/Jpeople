package com.gunwook.jpeople.goodlike.repository;

import com.gunwook.jpeople.goodlike.entity.GoodLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoodLikeRepository extends JpaRepository<GoodLike, Long> {
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    boolean existsByUserIdAndCommentId(Long userId, Long commentId);
    Optional<GoodLike> findByUserIdAndPostId(Long userId, Long postId);
    Optional<GoodLike> findByUserIdAndCommentId(Long userId, Long commentId);
}
