package org.ppocharong.muckstagram.repository;

import org.ppocharong.muckstagram.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost_PostId(Long postId);  // 특정 게시글의 댓글 조회

    long countByPost_PostId(Long postId);  // 특정 포스트의 댓글 수 계산
}

