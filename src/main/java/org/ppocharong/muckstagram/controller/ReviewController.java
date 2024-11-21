package org.ppocharong.muckstagram.controller;

import org.ppocharong.muckstagram.model.Review;
import org.ppocharong.muckstagram.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller  // RestController 대신 Controller 사용
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 리뷰 작성 처리
    @PostMapping
    public String addReview(@RequestParam Long restaurantId,
                            @RequestParam Long userId,
                            @RequestParam int rating,
                            @RequestParam String text) {
        // 리뷰 저장 로직
        reviewService.addReview(restaurantId, userId, rating, text);

        // 해당 레스토랑 페이지로 리다이렉트
        return "redirect:/restaurants/" + restaurantId;  // 리다이렉트 처리
    }

    // 특정 식당의 리뷰 가져오기
    @GetMapping("/{restaurantId}")
    @ResponseBody  // JSON 반환 시 명시적으로 사용
    public ResponseEntity<List<Review>> getReviews(@PathVariable Long restaurantId) {
        List<Review> reviews = reviewService.getReviewsByRestaurantId(restaurantId);
        if (reviews.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // 리뷰가 없는 경우 204 No Content 반환
        }
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }
}
