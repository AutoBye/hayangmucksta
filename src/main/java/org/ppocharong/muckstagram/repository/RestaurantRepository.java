package org.ppocharong.muckstagram.repository;

import org.ppocharong.muckstagram.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    // 기본적인 CRUD 작업은 JpaRepository가 제공

    @Query("SELECT r FROM Restaurant r LEFT JOIN r.reviews rev GROUP BY r ORDER BY COALESCE(AVG(rev.rating), 0) DESC")
    Page<Restaurant> findAllWithRatings(Pageable pageable);

    // 음식점을 가나다 순으로 정렬하여 조회
    @Query("SELECT r FROM Restaurant r ORDER BY r.name ASC")
    Page<Restaurant> findAllByName(Pageable pageable);

    @Query("SELECT r FROM Restaurant r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Restaurant> findByNameContainingIgnoreCase(@Param("search") String search, Pageable pageable);

    @Query("SELECT r FROM Restaurant r LEFT JOIN r.reviews rev WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%')) GROUP BY r ORDER BY COALESCE(AVG(rev.rating), 0) DESC")
    Page<Restaurant> findWithRatingsAndSearch(@Param("search") String search, Pageable pageable);
}
