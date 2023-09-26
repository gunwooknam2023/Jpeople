package com.gunwook.jpeople.mypage.controller;

import com.gunwook.jpeople.comment.dto.CommentResponseDto;
import com.gunwook.jpeople.mypage.dto.ProfileResponseDto;
import com.gunwook.jpeople.mypage.service.MyPageService;
import com.gunwook.jpeople.post.dto.PostResponseDto;
import com.gunwook.jpeople.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MypageController {
    private final MyPageService myPageService;

    /**
     * 프로필 조회
     * @param userDetails 요청한 유저 정보
     * @return 유저 정보 반환
     */
    @GetMapping("/mypage/profile")
    ResponseEntity<ProfileResponseDto> getMyProfiles(@AuthenticationPrincipal UserDetailsImpl userDetails){
        ProfileResponseDto profileResponseDto = myPageService.getMyProfiles(userDetails.getUser());
        return ResponseEntity.ok(profileResponseDto);
    }

    /**
     * 작성한 게시글 조회
     * @param userDetails 요청한 유저 정보
     * @return 게시글 리스트 반환
     */
    @GetMapping("/mypage/posts")
    ResponseEntity<List<PostResponseDto>> getMyPosts(@AuthenticationPrincipal UserDetailsImpl userDetails){
        List<PostResponseDto> postResponseDtos = myPageService.getMyPosts(userDetails.getUser());
        return ResponseEntity.ok(postResponseDtos);
    }

    /**
     * 작성한 댓글 조회
     * @param userDetails 요청한 유저 정보
     * @return 댓글 리스트 반환
     */
    @GetMapping("/mypage/comments")
    ResponseEntity<List<CommentResponseDto>> getMyComments(@AuthenticationPrincipal UserDetailsImpl userDetails){
        List<CommentResponseDto> commentResponseDtos = myPageService.getMyComments(userDetails.getUser());
        return ResponseEntity.ok(commentResponseDtos);
    }




}
