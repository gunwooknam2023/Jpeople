package com.gunwook.jpeople.report.controller;

import com.gunwook.jpeople.post.dto.PostResponseDto;
import com.gunwook.jpeople.report.dto.ReportRequestDto;
import com.gunwook.jpeople.report.dto.ReportResponseDto;
import com.gunwook.jpeople.report.service.ReportService;
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
public class ReportController {

    private final ReportService reportService;

    /**
     * 게시글 신고
     * @param userDetails 유저 정보
     * @param post_id 신고할 게시글 번호
     * @return 신고 성공/실패 여부
     */
    @PostMapping("/posts/{post_id}/report")
    ResponseEntity<String> reportPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @PathVariable Long post_id,
                                      @RequestBody ReportRequestDto reportRequestDto){
        String result = reportService.reportPost(userDetails.getUser(), post_id, reportRequestDto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/posts/report")
    ResponseEntity<List<ReportResponseDto>> getReportPost(@AuthenticationPrincipal UserDetailsImpl userDetails){
        List<ReportResponseDto> reportResponseDtos = reportService.getReportPost(userDetails.getUser());
        return ResponseEntity.ok(reportResponseDtos);
    }

//    /**
//     * 댓글 신고
//     * @param userDetails 유저 정보
//     * @param comment_id 신고할 댓글 번호
//     * @return 신고 성공/실패 여부
//     */
//    @PostMapping("/comments/{comment_id}/report")
//    ResponseEntity<String> reportComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
//                                         @PathVariable Long comment_id){
//        String result = reportService.reportComment(userDetails.getUser(), comment_id);
//        return ResponseEntity.ok(result);
//    }



}
