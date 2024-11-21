package org.ppocharong.muckstagram.repository;

import org.ppocharong.muckstagram.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByRestaurant_RestaurantId(Long restaurantId);

}
