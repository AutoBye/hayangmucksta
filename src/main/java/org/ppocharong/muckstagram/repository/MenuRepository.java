package org.ppocharong.muckstagram.repository;

import org.ppocharong.muckstagram.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByRestaurant_RestaurantId(Long restaurantId);
}
