package org.ppocharong.muckstagram.controller;

import jakarta.servlet.http.HttpSession;
import org.ppocharong.muckstagram.model.Ingredient;
import org.ppocharong.muckstagram.model.PreferredIngredient;
import org.ppocharong.muckstagram.model.User;
import org.ppocharong.muckstagram.repository.UserRepository;
import org.ppocharong.muckstagram.service.PreferredIngredientService;
import org.ppocharong.muckstagram.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PreferredIngredientService preferredIngredientService;

    public UserController(UserService userService, UserRepository userRepository, PreferredIngredientService preferredIngredientService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.preferredIngredientService = preferredIngredientService;
    }

    // "내 정보" 페이지
    @GetMapping("/my-info")
    public String myInfo(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");  // 세션에서 사용자 정보 가져오기

        if (user == null) {
            return "redirect:/login";  // 로그인되어 있지 않으면 로그인 페이지로 리다이렉트
        }

        model.addAttribute("username", user.getUsername());

        // 사용자 정보를 Model에 추가
        model.addAttribute("user", user);
        return "my-info";  // my-info.html로 이동
    }

    @PostMapping("/update-preferred-ingredients")
    public String updatePreferredIngredients(
            @RequestParam("ingredients") List<String> ingredientNames,
            HttpSession session) {

        // 세션에서 사용자 정보 가져오기
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // 사용자가 입력한 재료 목록을 처리
        List<PreferredIngredient> ingredients = new ArrayList<>();
        for (String name : ingredientNames) {
            PreferredIngredient ingredient = new PreferredIngredient();
            ingredient.setIngredientName(name);
            ingredients.add(ingredient);
        }

        // 선호 재료 업데이트
        preferredIngredientService.updatePreferredIngredients(user, ingredients);

        // 세션에 사용자 정보 다시 저장하기 전에 데이터베이스에서 새로 불러오기
        User updatedUser = userService.findUserById(user.getUserId()); // DB에서 갱신된 사용자 정보 가져오기
        session.setAttribute("user", updatedUser);  // 갱신된 사용자 정보를 세션에 저장

        return "redirect:/my-info";  // 업데이트 후 내 정보 페이지로 리다이렉트
    }


}
