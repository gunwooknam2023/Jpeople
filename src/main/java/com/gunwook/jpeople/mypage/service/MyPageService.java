package com.gunwook.jpeople.mypage.service;

import com.gunwook.jpeople.comment.dto.CommentResponseDto;
import com.gunwook.jpeople.comment.entity.Comment;
import com.gunwook.jpeople.comment.repository.CommentRepository;
import com.gunwook.jpeople.mypage.dto.ProfileModifyRequestDto;
import com.gunwook.jpeople.mypage.dto.ProfileResponseDto;
import com.gunwook.jpeople.post.dto.PostResponseDto;
import com.gunwook.jpeople.post.entity.Category;
import com.gunwook.jpeople.post.entity.Post;
import com.gunwook.jpeople.post.repository.PostRepository;
import com.gunwook.jpeople.post.service.S3Uploader;
import com.gunwook.jpeople.user.entity.User;
import com.gunwook.jpeople.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public String introductionModify(User user, ProfileModifyRequestDto profileModifyRequestDto) {
        User requestUser = userRepository.findById(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("로그인 후 사용하세요.")
        );

        requestUser.updateIntroduction(profileModifyRequestDto);
        return "프로필 변경이 완료되었습니다.";
    }

    @Transactional
    public String uploadProfileImage(MultipartFile file, User user) throws IOException {
        User requestUser = userRepository.findById(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("로그인 후 사용하세요.")
        );

        String ProfileUrl = s3Uploader.upload(file, "profile");
        requestUser.setProfileUrl(ProfileUrl);

        return "프로필 사진 업로드에 성공하였습니다.";
    }


    @Transactional
    public String deleteProfileImage(User user) {
        User requestUser = userRepository.findById(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("로그인 후 사용하세요.")
        );

        String s3Url = user.getProfileUrl();
        String s3Key = s3Url.substring(s3Url.indexOf("profile/"));

        s3Uploader.fileDelete(s3Key);
        requestUser.setProfileUrl("/images/logo.png");

        return "프로필 사진 제거에 성공했습니다.";
    }

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
