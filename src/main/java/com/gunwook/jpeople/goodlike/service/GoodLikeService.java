package com.gunwook.jpeople.goodlike.service;

import com.gunwook.jpeople.comment.dto.CommentRequestDto;
import com.gunwook.jpeople.comment.entity.Comment;
import com.gunwook.jpeople.comment.repository.CommentRepository;
import com.gunwook.jpeople.goodlike.entity.GoodLike;
import com.gunwook.jpeople.goodlike.repository.GoodLikeRepository;
import com.gunwook.jpeople.post.entity.Post;
import com.gunwook.jpeople.post.repository.PostRepository;
import com.gunwook.jpeople.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoodLikeService {
    private final GoodLikeRepository goodLikeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public String goodLikePost(User user, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        if(user.getId().equals(post.getUser().getId())){
            throw new IllegalArgumentException("본인의 게시글에는 좋아요를 누를수없습니다.");
        }

        if(!goodLikeRepository.existsByUserIdAndPostId(user.getId(), postId)){
            GoodLike goodLike = new GoodLike(user, post);
            goodLikeRepository.save(goodLike);
            post.likePost();
            return "게시글에 좋아요를 누르셨습니다.";
        } else{
            GoodLike goodLike = goodLikeRepository.findByUserIdAndPostId(user.getId(), postId).orElseThrow(
                    () -> new IllegalArgumentException("좋아요 취소에 실패하였습니다.")
            );
            goodLikeRepository.delete(goodLike);
            post.deleteLikePost();
            return "게시글의 좋아요를 취소하셨습니다.";
        }
    }

    @Transactional
    public String goodLikeComment(User user, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );

        if(user.getId().equals(comment.getUser().getId())){
            throw new IllegalArgumentException("본인의 댓글에는 좋아요를 누를수없습니다.");
        }

        if(!goodLikeRepository.existsByUserIdAndCommentId(user.getId(), commentId)){
            GoodLike goodLike = new GoodLike(user, comment);
            goodLikeRepository.save(goodLike);
            comment.likeComment();
            return "댓글에 좋아요를 누르셨습니다.";
        } else {
            GoodLike goodLike = goodLikeRepository.findByUserIdAndCommentId(user.getId(), commentId).orElseThrow(
                    () -> new IllegalArgumentException("좋아요 취소에 실패하였습니다.")
            );
            goodLikeRepository.delete(goodLike);
            comment.deleteLikeComment();
            return "댓글의 좋아요를 취소하셨습니다.";
        }
    }
}
