package com.gunwook.jpeople.mypage.service;

import com.gunwook.jpeople.comment.dto.CommentResponseDto;
import com.gunwook.jpeople.comment.entity.Comment;
import com.gunwook.jpeople.comment.repository.CommentRepository;
import com.gunwook.jpeople.mypage.dto.ProfileResponseDto;
import com.gunwook.jpeople.post.dto.PostResponseDto;
import com.gunwook.jpeople.post.entity.Category;
import com.gunwook.jpeople.post.entity.Post;
import com.gunwook.jpeople.post.repository.PostRepository;
import com.gunwook.jpeople.user.entity.User;
import com.gunwook.jpeople.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public ProfileResponseDto getMyProfiles(User user) {
        userRepository.findById(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("로그인 후 사용하세요.")
        );

        ProfileResponseDto profileResponseDto = new ProfileResponseDto(user);
        return profileResponseDto;
    }

    public List<PostResponseDto> getMyPosts(User user) {
        userRepository.findById(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("로그인 후 사용하세요.")
        );

        // 게시글 받아오기
        List<Post> posts = postRepository.findByCategoryAndUserIdOrderByCreatedAtDesc(Category.FREE_BOARD, user.getId());
        List<PostResponseDto> postResponseDtos = new ArrayList<>();


        for(Post post : posts){
            PostResponseDto postResponseDto = new PostResponseDto(post);
            postResponseDtos.add(postResponseDto);
        }

        return postResponseDtos;
    }

    public List<CommentResponseDto> getMyComments(User user) {
        userRepository.findById(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("로그인 후 사용하세요.")
        );

        // 댓글 받아오기
        List<Comment> comments = commentRepository.findByUserId(user.getId());
        Collections.reverse(comments);

        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();


        for(Comment comment : comments){
            CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
            commentResponseDtos.add(commentResponseDto);
        }

        return commentResponseDtos;

    }
}
