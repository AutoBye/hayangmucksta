package org.ppocharong.muckstagram.controller;

import jakarta.servlet.http.HttpSession;
import org.ppocharong.muckstagram.model.User;
import org.ppocharong.muckstagram.service.PreferredIngredientService;
import org.ppocharong.muckstagram.service.RecommendationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class RecommendationController {

    private final PreferredIngredientService preferredIngredientService;
    private final RecommendationService recommendationService;

    public RecommendationController(PreferredIngredientService preferredIngredientService,
                                    RecommendationService recommendationService) {
        this.preferredIngredientService = preferredIngredientService;
        this.recommendationService = recommendationService;
    }

    @GetMapping("/recommend-based-on-ingredients")
    public ResponseEntity<?> recommendBasedOnUserIngredients(HttpSession session) {
        User currentUser = (User) session.getAttribute("user");

        if (currentUser == null) {
            System.out.println("login null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        // 사용자 선호 재료 가져오기
        List<String> preferredIngredients = preferredIngredientService.getPreferredIngredientsByUser(currentUser);

        //System.out.println(preferredIngredients);
        // Flask 서버로 요청 전송하여 추천 결과 받기
        Map<String, Object> recommendationResult = recommendationService.recommendByIngredients(currentUser.getUserId());

        return ResponseEntity.ok(recommendationResult);
    }
}
