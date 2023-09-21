package com.gunwook.jpeople.post.controller;

import com.gunwook.jpeople.post.dto.PostRequestDto;
import com.gunwook.jpeople.post.dto.PostResponseDto;
import com.gunwook.jpeople.post.service.PostService;
import com.gunwook.jpeople.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    /**
     * 게시글 작성
     * @param postRequestDto 게시글 양식
     * @param userDetails 유저 정보
     * @return 게시글 생성 성공/실패 여부
     */
    @PostMapping("/posts")
    ResponseEntity<String> createPost(@RequestBody PostRequestDto postRequestDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        String result = postService.createPost(postRequestDto, userDetails.getUser());
        return ResponseEntity.ok(result);
    }

    /**
     * 게시글 수정
     * @param postRequestDto 수정할 내용
     * @param userDetails 유저 정보
     * @param post_id 수정할 게시글 번호
     * @return 게시글 수정 성공/실패 여부
     */
    @PutMapping("/posts/{post_id}")
    ResponseEntity<String> updatePost(@RequestBody PostRequestDto postRequestDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @PathVariable Long post_id){
        String result = postService.updatePost(postRequestDto, userDetails.getUser(), post_id);
        return ResponseEntity.ok(result);
    }

    /**
     * 게시글 삭제
     * @param userDetails 유저 정보
     * @param post_id 삭제할 게시글 번호
     * @return 게시글 삭제 성공/실패 여부
     */
    @DeleteMapping("/posts/{post_id}")
    ResponseEntity<String> deletePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @PathVariable Long post_id){
        String result = postService.deletePost(userDetails.getUser(), post_id);
        return ResponseEntity.ok(result);
    }

    /**
     * 게시글 전체 조회
     * @param userDetails 유저 정보
     * @return 게시글 정보
     */
    @GetMapping("/posts")
    ResponseEntity<List<PostResponseDto>> getPosts(@AuthenticationPrincipal UserDetailsImpl userDetails){
        List<PostResponseDto> postResponseDtos = postService.getPosts(userDetails.getUser());
        return ResponseEntity.ok(postResponseDtos);
    }

    /**
     * 게시글 단건 조회
     * @param userDetails 유저 정보
     * @param post_id 게시글 아이디
     * @return 게시글 정보
     */
    @GetMapping("/posts/{post_id}")
    ResponseEntity<PostResponseDto> getPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @PathVariable Long post_id){
        PostResponseDto postResponseDto = postService.getPost(userDetails.getUser(), post_id);
        return ResponseEntity.ok(postResponseDto);
    }
}
