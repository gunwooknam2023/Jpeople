package com.gunwook.jpeople.post.repository;

import com.gunwook.jpeople.post.entity.Category;
import com.gunwook.jpeople.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategoryOrderByCreatedAtDesc(Category category);
    List<Post> findByCategoryAndUserIdOrderByCreatedAtDesc(Category category, Long userId);
}

