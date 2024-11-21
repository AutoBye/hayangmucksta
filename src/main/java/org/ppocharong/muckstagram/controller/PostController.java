package org.ppocharong.muckstagram.controller;

import org.ppocharong.muckstagram.model.Post;
import org.ppocharong.muckstagram.service.PostService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시글 작성
    @PostMapping
    public void createPost(@RequestParam String title,
                           @RequestParam String content,
                           @RequestParam Long userId) {  // 사용자 ID를 받아서 Post에 연결
        postService.createPost(title, content, userId);
    }

    // 모든 게시글 조회
    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }
}
