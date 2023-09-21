package com.gunwook.jpeople.comment.service;

import com.gunwook.jpeople.comment.dto.CommentRequestDto;
import com.gunwook.jpeople.comment.entity.Comment;
import com.gunwook.jpeople.comment.repository.CommentRepository;
import com.gunwook.jpeople.post.dto.PostResponseDto;
import com.gunwook.jpeople.post.entity.Post;
import com.gunwook.jpeople.post.repository.PostRepository;
import com.gunwook.jpeople.user.entity.User;
import com.gunwook.jpeople.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;


    public String createComment(CommentRequestDto commentRequestDto, User user, Long post_id) {
        Post post = postRepository.findById(post_id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글 입니다.")
        );

        Comment comment = new Comment(commentRequestDto, user, post);
        commentRepository.save(comment);

        return "댓글이 생성되었습니다.";
    }

    @Transactional
    public String updateComment(CommentRequestDto commentRequestDto, User user, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글입니다.")
        );

        if(!comment.getUser().getId().equals(user.getId())){
            throw new IllegalArgumentException("댓글 작성자만 수정이 가능합니다.");
        }

        comment.updateComment(commentRequestDto);
        return "댓글이 수정되었습니다";
    }

    @Transactional
    public String deleteComment(User user, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글입니다.")
        );

        if(!comment.getUser().getId().equals(user.getId())){
            throw new IllegalArgumentException("댓글 작성자만 삭제가 가능합니다.");
        }

        commentRepository.delete(comment);
        return "댓글이 삭제되었습니다.";
    }
}
