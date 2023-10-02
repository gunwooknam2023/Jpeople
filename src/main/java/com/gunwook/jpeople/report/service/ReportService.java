package com.gunwook.jpeople.report.service;

import com.gunwook.jpeople.comment.entity.Comment;
import com.gunwook.jpeople.comment.repository.CommentRepository;
import com.gunwook.jpeople.post.entity.Post;
import com.gunwook.jpeople.post.repository.PostRepository;
import com.gunwook.jpeople.report.dto.ReportRequestDto;
import com.gunwook.jpeople.report.entity.Report;
import com.gunwook.jpeople.report.repository.ReportRepository;
import com.gunwook.jpeople.user.entity.User;
import com.gunwook.jpeople.user.entity.UserRoleEnum;
import com.gunwook.jpeople.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReportRepository reportRepository;

    @Transactional
    public String reportPost(User user, Long postId, ReportRequestDto reportRequestDto) {
        userRepository.findById(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("로그인 후 사용하세요.")
        );

        if(user.getRole().equals(UserRoleEnum.ADMIN)){
            throw new IllegalArgumentException("관리자는 신고가 불가능합니다.");
        }

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        if(user.getId().equals(post.getUser().getId())){
            throw new IllegalArgumentException("본인의 게시글은 신고가 불가능합니다");
        }

        Report report = new Report(post, user, reportRequestDto);
        reportRepository.save(report);
        return "신고가 완료되었습니다.";
    }

//    @Transactional
//    public String reportComment(User user, Long commentId) {
//        userRepository.findById(user.getId()).orElseThrow(
//                () -> new IllegalArgumentException("로그인 후 사용하세요.")
//        );
//
//        if(user.getRole().equals(UserRoleEnum.ADMIN)){
//            throw new IllegalArgumentException("관리자는 신고가 불가능합니다.");
//        }
//
//        Comment comment = commentRepository.findById(commentId).orElseThrow(
//                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
//        );
//
//        if(user.getId().equals(comment.getUser().getId())){
//            throw new IllegalArgumentException("본인의 댓글은 신고가 불가능합니다");
//        }
//
//        ReportRequestDto reportRequestDto = new ReportRequestDto()
//
//        Report report = new Report();
//        reportRepository.save(report);
//        return "신고가 완료되었습니다.";
//    }



}
