package com.gunwook.jpeople.comment.repository;

import com.gunwook.jpeople.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
