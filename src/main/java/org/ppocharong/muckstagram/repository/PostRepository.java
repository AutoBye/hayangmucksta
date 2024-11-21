package org.ppocharong.muckstagram.repository;

import org.ppocharong.muckstagram.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword);

    // 페이지에 따른 포스트를 가져오는 커스텀 쿼리
    @Query(value = "SELECT * FROM posts ORDER BY created_at DESC LIMIT :pageSize OFFSET :offset", nativeQuery = true)
    List<Post> findPostsByPage(int offset, int pageSize);

    // 이전 게시글 찾기 (현재 게시글 ID보다 작은 게시글 중 가장 큰 ID)
    Optional<Post> findFirstByPostIdLessThanOrderByPostIdDesc(Long postId);

    // 다음 게시글 찾기 (현재 게시글 ID보다 큰 게시글 중 가장 작은 ID)
    Optional<Post> findFirstByPostIdGreaterThanOrderByPostIdAsc(Long postId);
}
