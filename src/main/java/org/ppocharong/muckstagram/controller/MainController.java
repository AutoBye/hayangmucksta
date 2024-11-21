package org.ppocharong.muckstagram.controller;

import jakarta.servlet.http.HttpSession;
import org.ppocharong.muckstagram.model.Post;
import org.ppocharong.muckstagram.model.Restaurant;
import org.ppocharong.muckstagram.model.User;
import org.ppocharong.muckstagram.service.PostService;
import org.ppocharong.muckstagram.service.PreferredIngredientService;
import org.ppocharong.muckstagram.service.RestaurantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;  // Model 임포트

import java.util.List;

@Controller
public class MainController {

    private final PostService postService;
    private final RestaurantService restaurantService;

    // 생성자를 통한 의존성 주입
    public MainController(PostService postService, RestaurantService restaurantService) {
        this.postService = postService;
        this.restaurantService = restaurantService;
    }

    // 메인 페이지로 이동
    @GetMapping("/")
    public String mainPage(HttpSession session, Model model) {
        // 포스트와 레스토랑 목록을 가져옴
        List<Post> postList = postService.getAllPosts();
        List<Restaurant> restaurantList = restaurantService.getAllRestaurants();

        // 포스트 목록을 8개로 제한
        List<Post> limitedPosts = (postList != null && postList.size() > 8) ? postList.subList(0, 8) : postList;

        // 레스토랑 목록을 8개로 제한
        List<Restaurant> limitedRestaurants = (restaurantList != null && restaurantList.size() > 8) ? restaurantList.subList(0, 8) : restaurantList;

        // 모델에 데이터를 추가하여 뷰로 전달
        model.addAttribute("posts", limitedPosts);  // 8개로 제한된 포스트 목록 추가
        model.addAttribute("restaurants", limitedRestaurants);  // 8개로 제한된 레스토랑 목록 추가

        // 세션에서 사용자 정보 가져오기
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("username", user.getUsername());
        }

        return "main";  // main.html로 이동
    }

    // Community 페이지로 이동
    @GetMapping("/community")
    public String communityPage(HttpSession session, Model model) {
        // 세션에서 사용자 정보 가져오기
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("username", user.getUsername());
        }
        return "community";  // community.html 또는 community.jsp로 이동
    }

    // Login 페이지로 이동
    @GetMapping("/login")
    public String loginPage() {
        return "login";  // login.html 또는 login.jsp로 이동
    }

    // Signup 페이지로 이동
    @GetMapping("/signup")
    public String signupPage() {
        return "signup";  // signup.html 또는 signup.jsp로 이동
    }

}
