package org.ppocharong.muckstagram.controller;

import jakarta.servlet.http.HttpSession;
import org.ppocharong.muckstagram.model.User;
import org.ppocharong.muckstagram.service.PreferredIngredientService;
import org.ppocharong.muckstagram.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;


@Controller
public class AuthController {

    private final UserService userService;
    private final PreferredIngredientService preferredIngredientService;

    public AuthController(UserService userService, PreferredIngredientService preferredIngredientService) {
        this.userService = userService;
        this.preferredIngredientService = preferredIngredientService;
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String username,
                         @RequestParam String password,
                         @RequestParam String email,
                         @RequestParam String preferredIngredients,  // 선호 재료 입력값
                         Model model) {
        // 사용자 생성 및 저장
        try {
            // 사용자 저장
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);  // 비밀번호는 이후에 암호화
            userService.saveUser(user);

            // 선호 재료를 쉼표로 분리
            String[] ingredients = preferredIngredients.split(",");
            preferredIngredientService.savePreferredIngredients(user, ingredients);

            return "redirect:/login";  // 성공 시 로그인 페이지로 이동

        } catch (IllegalArgumentException e) {
            // 중복된 아이디나 이메일 처리
            model.addAttribute("errorMessage", e.getMessage());
            return "signup";  // 회원가입 페이지로 다시 이동
        }
    }


    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        // 사용자 조회
        Optional<User> userOptional = userService.findByUsername(username);

        if (userOptional.isPresent() && userService.checkPassword(userOptional.get(), password)) {
            // 로그인 성공 - 세션에 사용자 정보 저장
            session.setAttribute("user", userOptional.get());
            return "redirect:/";  // 메인 페이지로 리다이렉트
        } else {
            // 로그인 실패 시 에러 메시지 설정
            if (userOptional.isEmpty()) {
                model.addAttribute("errorMessage", "존재하지 않은 ID입니다.");
            } else {
                model.addAttribute("errorMessage", "ID 또는 비밀번호가 일치하지 않습니다.");
            }
            return "login";  // 로그인 페이지로 다시 이동
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 세션 무효화
        session.invalidate();
        return "redirect:/";  // 로그아웃 후 로그인 페이지로 이동
    }

}
