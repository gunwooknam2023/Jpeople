package com.gunwook.jpeople.goodlike.controller;


import com.gunwook.jpeople.goodlike.service.GoodLikeService;
import com.gunwook.jpeople.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GoodLikeController {
    private final GoodLikeService goodLikeService;

    /**
     * 게시글 좋아요
     * @param userDetails 유저 정보
     * @param post_id 게시글 아이디
     * @return 게시글 좋아요 성공/실패 여부
     */
    @PostMapping("/posts/goodlike/{post_id}")
    ResponseEntity<String> goodLikePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                        @PathVariable Long post_id){
        String result = goodLikeService.goodLikePost(userDetails.getUser(), post_id);
        return ResponseEntity.ok(result);
    }

    /**
     * 댓글 좋아요
     * @param userDetails 유저 정보
     * @param comment_id 댓글 아이디
     * @return 게시글 좋아요 성공/실패 여부
     */
    @PostMapping("/comments/goodlike/{comment_id}")
    ResponseEntity<String> goodLikeComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @PathVariable Long comment_id){
        String result = goodLikeService.goodLikeComment(userDetails.getUser(), comment_id);
        return ResponseEntity.ok(result);
    }


}
