package com.gunwook.jpeople.comment.controller;

import com.gunwook.jpeople.comment.dto.CommentRequestDto;
import com.gunwook.jpeople.comment.service.CommentService;
import com.gunwook.jpeople.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    /**
     * 댓글 생성
     * @param commentRequestDto 댓글 내용
     * @param userDetails 유저 정보
     * @return 댓글 생성 성공/실패 여부
     */
    @PostMapping("/comments/{post_id}")
    ResponseEntity<String> createComment(@RequestBody CommentRequestDto commentRequestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @PathVariable Long post_id){
        String result = commentService.createComment(commentRequestDto, userDetails.getUser(), post_id);
        return ResponseEntity.ok(result);
    }

    /**
     * 댓글 수정
     * @param commentRequestDto 댓글 내용
     * @param userDetails 유저 정보
     * @param comment_id 수정할 댓글 번호
     * @return 댓글 수정 성공/실패 여부
     */
    @PutMapping("/comments/{comment_id}")
    ResponseEntity<String> updateComment(@RequestBody CommentRequestDto commentRequestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @PathVariable Long comment_id){
        String result = commentService.updateComment(commentRequestDto, userDetails.getUser(), comment_id);
        return ResponseEntity.ok(result);
    }

    /**
     * 댓글 삭제
     * @param userDetails 유저 정보
     * @param comment_id 삭제할 댓글 번호
     * @return 댓글 삭제 성공/실패 여부
     */
    @DeleteMapping("/comments/{comment_id}")
    ResponseEntity<String> deleteComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @PathVariable Long comment_id){
        String result = commentService.deleteComment(userDetails.getUser(), comment_id);
        return ResponseEntity.ok(result);
    }
}
