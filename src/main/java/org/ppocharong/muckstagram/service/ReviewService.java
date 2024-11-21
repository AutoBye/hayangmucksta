package org.ppocharong.muckstagram.service;

import org.ppocharong.muckstagram.model.Restaurant;
import org.ppocharong.muckstagram.model.Review;
import org.ppocharong.muckstagram.model.User;
import org.ppocharong.muckstagram.repository.RestaurantRepository;
import org.ppocharong.muckstagram.repository.ReviewRepository;
import org.ppocharong.muckstagram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    // 특정 식당의 리뷰 목록 조회
    public List<Review> getReviewsByRestaurantId(Long restaurantId) {
        return reviewRepository.findByRestaurant_RestaurantId(restaurantId);
    }

    // 리뷰 추가 메서드
    public void addReview(Long restaurantId, Long userId, int rating, String text) {
        // restaurantId로 식당 찾기
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid restaurant ID: " + restaurantId));

        // userId로 사용자 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));

        // 새로운 리뷰 객체 생성
        Review review = new Review();
        review.setRestaurant(restaurant);
        review.setUser(user);
        review.setRating(rating);
        review.setText(text);

        // 리뷰 저장
        reviewRepository.save(review);
    }

    public double calculateAverageRating(Long restaurantId) {
// 해당 레스토랑의 모든 리뷰를 가져옴
        List<Review> reviews = reviewRepository.findByRestaurant_RestaurantId(restaurantId);

        if (reviews.isEmpty()) {
            return 0.0; // 리뷰가 없을 경우 평균 0점 반환
        }

        return reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);
    }
}
