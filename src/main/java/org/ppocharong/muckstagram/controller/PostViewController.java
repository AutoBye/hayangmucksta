package org.ppocharong.muckstagram.controller;

import jakarta.servlet.http.HttpSession;
import org.ppocharong.muckstagram.exception.ResourceNotFoundException;
import org.ppocharong.muckstagram.model.Comment;
import org.ppocharong.muckstagram.model.Post;
import org.ppocharong.muckstagram.model.Restaurant;
import org.ppocharong.muckstagram.model.User;
import org.ppocharong.muckstagram.service.CommentService;
import org.ppocharong.muckstagram.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/posts")
public class PostViewController {
    private final PostService postService;
    private final CommentService commentService;
    public PostViewController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    // 포스트 목록 페이지 렌더링
    @GetMapping
    public String showPosts(@RequestParam(defaultValue = "1") int page, HttpSession session, Model model) {
        // 한 페이지에 표시할 포스트 수
        int pageSize = 10;

        // 전체 포스트 수
        long totalPosts = postService.countPosts();
        int totalPages = (int) Math.ceil((double) totalPosts / pageSize);

        // 해당 페이지에 보여줄 포스트 리스트 가져오기
        List<Post> posts = postService.getPostsByPage(page, pageSize);

        // 각 포스트에 대해 댓글 수 계산
        Map<Long, Long> commentCounts = posts.stream()
                .collect(Collectors.toMap(Post::getPostId, post -> commentService.countCommentsByPost(post.getPostId())));

        // 세션에서 사용자 정보 가져오기
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("username", user.getUsername());
        }

        // 모델에 페이지 관련 속성 추가
        model.addAttribute("posts", posts);
        model.addAttribute("commentCounts", commentCounts);  // 댓글 수 전달
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages); // totalPages 값을 넘김

        return "posts";
    }

    @GetMapping("/{id}")
    public String getPostById(@PathVariable Long id, HttpSession session, Model model) {
        // 세션에서 사용자 정보 가져오기
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("username", user.getUsername());
        }

        // ID로 포스트 찾기
        Optional<Post> postOptional = Optional.ofNullable(postService.getPostById(id));
        if (postOptional.isPresent()) {
            model.addAttribute("post", postOptional.get());

            // 해당 포스트의 댓글 목록 가져오기
            List<Comment> comments = commentService.getCommentsByPostId(id);
            model.addAttribute("comments", comments);

            // 이전글, 다음글 찾기
            Post previousPost = postService.getPreviousPost(id);
            Post nextPost = postService.getNextPost(id);
            model.addAttribute("previousPost", previousPost);
            model.addAttribute("nextPost", nextPost);

            return "post";  // post.html로 이동
        } else {
            return "main";  // 포스트가 없으면 404 페이지로 이동
        }
    }

    @GetMapping("/search")
    public String searchPosts(@RequestParam String keyword, Model model, HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("username", user.getUsername());
        }

        // 검색된 포스트 가져오기
        List<Post> searchResults = postService.searchPostsByKeyword(keyword);
        model.addAttribute("posts", searchResults);
        model.addAttribute("keyword", keyword);

        return "posts";  // 검색 결과 페이지로 이동
    }

    // 포스트 작성 페이지로 이동 (로그인된 사용자만 접근 가능)
    @GetMapping("/write")
    public String showWritePage(HttpSession session, Model model) {
        // 세션에서 사용자 정보 가져오기
        User user = (User) session.getAttribute("user");
        if (user == null) {
            // 로그인이 되어 있지 않으면 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }

        model.addAttribute("username", user.getUsername());
        return "write-post";  // post-write.html 템플릿으로 이동
    }

    @GetMapping("/edit")
    public String showEditPage(@RequestParam Long postId, HttpSession session, Model model) {
        // 세션에서 사용자 정보 가져오기
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("username", user.getUsername());
        }

        // ID로 포스트 찾기
        Optional<Post> postOptional = Optional.ofNullable(postService.getPostById(postId));
        if (postOptional.isPresent()) {
            model.addAttribute("post", postOptional.get());
            return "edit-post";  // edit-post.html로 이동
        } else {
            return "redirect:/posts";  // 포스트가 없으면 포스트 목록으로 리다이렉트
        }
    }

    @PostMapping("/edit")
    public String editPost(@RequestParam Long postId, @RequestParam String title, @RequestParam String content, HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        User user = (User) session.getAttribute("user");
        if (user == null) {
            // 로그인이 되어 있지 않으면 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }

        // 포스트 수정
        postService.editPost(postId, title, content);
        return "redirect:/posts";  // 수정 후 포스트 목록으로 리다이렉트
    }

    @PostMapping("/delete")
    public String deletePost(@RequestParam Long postId) {
        postService.deletePost(postId);
        return "redirect:/posts";  // 삭제 후 포스트 목록으로 리다이렉트
    }

//Optional<Post> postOptional = postService.findById(postId);
    // 댓글 작성 처리
@PostMapping("/comments")
public String addComment(@RequestParam Long postId, @RequestParam String commentContent, HttpSession session, RedirectAttributes redirectAttributes) {
    // 세션에서 사용자 정보 가져오기
    User user = (User) session.getAttribute("user");
    if (user == null) {
        return "redirect:/login";  // 로그인이 되어 있지 않으면 로그인 페이지로 리다이렉트
    }

    // 해당 포스트 가져오기
    Optional<Post> postOptional = postService.findById(postId);
    if (postOptional.isEmpty()) {
        return "redirect:/posts";  // 포스트가 없으면 목록으로 리다이렉트
    }

    // 댓글 저장
    Comment comment = new Comment();
    comment.setContent(commentContent);
    comment.setPost(postOptional.get());
    comment.setUser(user);
    commentService.saveComment(comment);

    // 성공 메시지
    redirectAttributes.addFlashAttribute("message", "댓글이 성공적으로 작성되었습니다!");

    // 댓글 작성 후 해당 포스트로 리다이렉트 (PRG 패턴 적용)
    return "redirect:/posts/" + postId;
}

    // 댓글 삭제 처리
    @PostMapping("/comments/delete")
    public String deleteComment(@RequestParam Long commentId, @RequestParam Long postId, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            // 로그인이 되어있지 않으면 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }

        // 댓글 삭제 로직 처리
        try {
            commentService.deleteComment(commentId, user.getUsername());
            redirectAttributes.addFlashAttribute("message", "댓글이 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "댓글 삭제 중 오류가 발생했습니다.");
        }

        // 댓글 삭제 후 원래 게시글로 리다이렉트
        return "redirect:/posts/" + postId;
    }


}
