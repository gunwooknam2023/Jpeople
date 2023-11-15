package com.gunwook.jpeople.comment.service;

import com.gunwook.jpeople.alarm.entity.Alarm;
import com.gunwook.jpeople.alarm.repository.AlarmRepository;
import com.gunwook.jpeople.alarm.service.AlarmService;
import com.gunwook.jpeople.comment.dto.CommentRequestDto;
import com.gunwook.jpeople.comment.entity.Comment;
import com.gunwook.jpeople.comment.repository.CommentRepository;
import com.gunwook.jpeople.post.dto.PostResponseDto;
import com.gunwook.jpeople.post.entity.Post;
import com.gunwook.jpeople.post.repository.PostRepository;
import com.gunwook.jpeople.user.entity.User;
import com.gunwook.jpeople.user.entity.UserRoleEnum;
import com.gunwook.jpeople.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;
    private final AlarmService alarmService;


    @Transactional
    public String createComment(CommentRequestDto commentRequestDto, User user, Long post_id) {
        Post post = postRepository.findById(post_id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글 입니다.")
        );

        Comment comment = new Comment(commentRequestDto, user, post);
        commentRepository.save(comment);

        // 댓글 남겼을 시 알림 생성
        if(!user.getId().equals(post.getUser().getId())) {
            Alarm alarm = new Alarm();
            alarm.setContents("\uD83D\uDCAC댓글 알림\uD83D\uDCAC<br>" + user.getNickname() + "님이 회원님의 <" + post.getTitle() + "> 게시글에 댓글을 남겼습니다.");
            alarm.setAddress("/api/view/boarddetail?id=" + post.getId());
            alarm.setRead(false);
            alarm.setUser(post.getUser());

            alarmRepository.save(alarm);
            alarmService.sendAlarmToUser(post.getUser().getId(), alarm);
        }

        return "댓글이 생성되었습니다.";
    }

    @Transactional
    public String updateComment(CommentRequestDto commentRequestDto, User user, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글입니다.")
        );

        if(!comment.getUser().getId().equals(user.getId())){
            throw new IllegalArgumentException("댓글 작성자만 수정이 가능합니다.");
        }

        comment.updateComment(commentRequestDto);
        return "댓글이 수정되었습니다";
    }

    @Transactional
    public String deleteComment(User user, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 댓글입니다.")
        );

        if(comment.getUser().getId().equals(user.getId()) || user.getRole().equals(UserRoleEnum.ADMIN)){
            commentRepository.delete(comment);
            return "댓글이 삭제되었습니다.";
        } else{
            throw new IllegalArgumentException("댓글 작성자만 삭제가 가능합니다.");
        }
    }

    public Boolean getCommentUser(User user, Long commentId) {
        Boolean result;
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );

        if(user.getId().equals(comment.getUser().getId()) || user.getRole().equals(UserRoleEnum.ADMIN)){
            result = true;
        } else{
            result = false;
        }

        return result;
    }
}
