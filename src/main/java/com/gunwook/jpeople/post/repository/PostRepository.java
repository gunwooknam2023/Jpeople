package com.gunwook.jpeople.post.repository;

import com.gunwook.jpeople.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
