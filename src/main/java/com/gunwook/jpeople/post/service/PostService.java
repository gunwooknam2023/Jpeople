package com.gunwook.jpeople.post.service;

import com.gunwook.jpeople.post.dto.PostRequestDto;
import com.gunwook.jpeople.post.dto.PostResponseDto;
import com.gunwook.jpeople.post.entity.Category;
import com.gunwook.jpeople.post.entity.Post;
import com.gunwook.jpeople.post.repository.PostRepository;
import com.gunwook.jpeople.user.entity.User;
import com.gunwook.jpeople.user.entity.UserRoleEnum;
import com.gunwook.jpeople.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public String createPost(PostRequestDto postRequestDto, User user) {
        // 권한 조회
        userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("로그인 후 사용하세요")
        );

        Post post = new Post(postRequestDto, user);
        post.setCategoryBoard();
        postRepository.save(post);
        return "게시글이 생성되었습니다.";
    }

    public String createNotificationPost(PostRequestDto postRequestDto, User user) {
        // 권한 조회
        userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("로그인 후 사용하세요")
        );

        if(!user.getRole().equals(UserRoleEnum.ADMIN)){
            throw new IllegalArgumentException("관리자가 아닙니다.");
        }

        Post post = new Post(postRequestDto, user);
        post.setCategoryNotification();
        postRepository.save(post);
        return "게시글이 생성되었습니다.";
    }

    @Transactional
    public String updatePost(PostRequestDto postRequestDto, User user, Long postId) {
        // 게시글 받아오기
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        // 작성자 일치 여부
        if(!post.getUser().getId().equals(user.getId())){
            throw new IllegalArgumentException("게시글 작성자만 수정이 가능합니다.");
        }

        post.updatePost(postRequestDto);
        return "게시글이 수정되었습니다.";
    }

    @Transactional
    public String deletePost(User user, Long postId) {
        // 게시글 받아오기
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        // 작성자 일치 여부
        if(!post.getUser().getId().equals(user.getId())){
            throw new IllegalArgumentException("게시글 작성자만 삭제가 가능합니다.");
        }

        postRepository.delete(post);
        return "게시글이 삭제되었습니다.";
    }

    public List<PostResponseDto> getPosts() {
        // 권한 조회
//        userRepository.findByUsername(user.getUsername()).orElseThrow(
//                () -> new IllegalArgumentException("로그인 후 사용하세요")
//        );

        // 게시글 받아오기
        List<Post> posts = postRepository.findByCategoryOrderByCreatedAtDesc(Category.FREE_BOARD);
        List<PostResponseDto> postResponseDtos = new ArrayList<>();

        for(Post post : posts){
            PostResponseDto postResponseDto = new PostResponseDto(post);
            postResponseDtos.add(postResponseDto);
        }

        return postResponseDtos;
    }

    public List<PostResponseDto> getNotificationPosts() {
        // 권한 조회
//        userRepository.findByUsername(user.getUsername()).orElseThrow(
//                () -> new IllegalArgumentException("로그인 후 사용하세요")
//        );

        // 게시글 받아오기
        List<Post> posts = postRepository.findByCategoryOrderByCreatedAtDesc(Category.NOTIFICATION_BOARD);
        List<PostResponseDto> postResponseDtos = new ArrayList<>();


        for(Post post : posts){
            PostResponseDto postResponseDto = new PostResponseDto(post);
            postResponseDtos.add(postResponseDto);
        }

        return postResponseDtos;
    }

    @Transactional
    public PostResponseDto getPost(User user, Long postId) {
        // 권한 조회
//        userRepository.findByUsername(user.getUsername()).orElseThrow(
//                () -> new IllegalArgumentException("로그인 후 사용하세요")
//        );

        // 게시글 받아오기
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        post.viewCnt();
        PostResponseDto postResponseDto = new PostResponseDto(post);
        return postResponseDto;
    }
}
