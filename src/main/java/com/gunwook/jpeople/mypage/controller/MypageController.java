package com.gunwook.jpeople.mypage.controller;

import com.gunwook.jpeople.comment.dto.CommentResponseDto;
import com.gunwook.jpeople.mypage.dto.ProfileResponseDto;
import com.gunwook.jpeople.mypage.dto.UpdateIntroduction;
import com.gunwook.jpeople.mypage.dto.UpdateNickname;
import com.gunwook.jpeople.mypage.service.MyPageService;
import com.gunwook.jpeople.post.dto.PostResponseDto;
import com.gunwook.jpeople.post.service.S3Uploader;
import com.gunwook.jpeople.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MypageController {
    private final MyPageService myPageService;

    /**
     * 닉네임 변경
     * @param userDetails 로그인 정보
     * @param nickname 변경할 닉네임
     * @return 성공/실패 여부
     */
    @PutMapping("/mypage/profile/nickname")
    public ResponseEntity<String> updateNickname(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @Valid @RequestBody UpdateNickname nickname){
        String result = myPageService.updateNickname(userDetails.getUser(), nickname);
        return ResponseEntity.ok(result);
    }

    /**
     * 자기소개 변경
     * @param userDetails 로그인 정보
     * @param introduction 변경할 자기소개
     * @return 성공/실패 여부
     */
    @PutMapping("/mypage/profile/introduction")
    public ResponseEntity<String> updateIntroduction(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @Valid @RequestBody UpdateIntroduction introduction){
        String result = myPageService.updateIntroduction(userDetails.getUser(), introduction);
        return ResponseEntity.ok(result);
    }

    /**
     * 프로필 사진 업로드(변경)
     * @param file 이미지 파일
     * @param userDetails 로그인 정보
     * @return 결과 반환
     * @throws IOException
     */
    @PostMapping("/mypage/profile/upload")
    public ResponseEntity<String> uploadProfileImage(@RequestParam("profileimage")MultipartFile file,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        String result = myPageService.uploadProfileImage(file, userDetails.getUser());
        return ResponseEntity.ok(result);
    }

    /**
     * 프로필 사진 제거(기본 이미지로 변경)
     * @param userDetails 로그인 정보
     * @return 결과 반환
     */
    @DeleteMapping("/mypage/profile/delete")
    public ResponseEntity<String> deleteProfileImage(@AuthenticationPrincipal UserDetailsImpl userDetails){
        String result = myPageService.deleteProfileImage(userDetails.getUser());
        return ResponseEntity.ok(result);
    }

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
