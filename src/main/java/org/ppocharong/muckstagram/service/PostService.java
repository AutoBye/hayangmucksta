package org.ppocharong.muckstagram.service;

import org.ppocharong.muckstagram.model.Post;
import org.ppocharong.muckstagram.model.User;
import org.ppocharong.muckstagram.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;  // UserService 추가

    @Autowired
    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    // 게시글 생성
    public void createPost(String title, String content, Long userId) {
        User user = userService.findUserById(userId);  // 사용자 정보 조회
        if (user == null) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUser(user);  // 게시글과 사용자 연관 설정

        postRepository.save(post);
    }

    // 모든 게시글 조회
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // 특정 ID로 게시글 조회
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }



    public Page<Post> getPostsPage(int page, int size) {
        return postRepository.findAll(PageRequest.of(page - 1, size));
    }

    // 키워드를 이용한 게시글 검색 메서드
    public List<Post> searchPostsByKeyword(String keyword) {
        return postRepository.findByTitleContainingOrContentContaining(keyword, keyword);
    }

    // 전체 포스트 수를 반환하는 메서드
    public long countPosts() {
        return postRepository.count();
    }

    // 페이지에 따른 포스트 목록 반환
    public List<Post> getPostsByPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return postRepository.findPostsByPage(offset, pageSize);
    }

    public void editPost(Long postId, String title, String content) {
        Post post = getPostById(postId);
        post.setTitle(title);
        post.setContent(content);
        postRepository.save(post);  // JPA를 사용하여 수정된 포스트 저장
    }

    public void deletePost(Long postId) {
        postRepository.deleteById(postId);  // JPA를 사용하여 포스트 삭제
    }

    // ID로 포스트 찾기
    public Optional<Post> findById(Long postId) {
        return postRepository.findById(postId);
    }

    // 이전 게시글 가져오기
    public Post getPreviousPost(Long currentPostId) {
        Optional<Post> previousPost = postRepository.findFirstByPostIdLessThanOrderByPostIdDesc(currentPostId);
        return previousPost.orElse(null);  // 이전 게시글이 없으면 null 반환
    }

    // 다음 게시글 가져오기
    public Post getNextPost(Long currentPostId) {
        Optional<Post> nextPost = postRepository.findFirstByPostIdGreaterThanOrderByPostIdAsc(currentPostId);
        return nextPost.orElse(null);  // 다음 게시글이 없으면 null 반환
    }
}
