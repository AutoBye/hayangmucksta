package org.ppocharong.muckstagram.service;

import org.ppocharong.muckstagram.exception.ResourceNotFoundException;
import org.ppocharong.muckstagram.model.Comment;
import org.ppocharong.muckstagram.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    // 댓글 저장
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    // 특정 게시글의 모든 댓글 가져오기
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPost_PostId(postId);
    }

    public long countCommentsByPost(Long postId) {
        return commentRepository.countByPost_PostId(postId);
    }

    // 댓글 삭제 메서드
    public void deleteComment(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("댓글을 찾을 수 없습니다."));

        // 현재 로그인한 사용자가 댓글 작성자인지 확인
        if (!comment.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("댓글 삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);  // 댓글 삭제
    }
}
