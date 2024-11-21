package org.ppocharong.muckstagram.service;

import org.ppocharong.muckstagram.model.Restaurant;
import org.ppocharong.muckstagram.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    // 모든 식당 조회
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    // 식당 저장
    public void saveRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    // 특정 ID로 식당 조회
    public Restaurant findRestaurantById(Long id) {
        return restaurantRepository.findById(id).orElse(null);
    }

    public Page<Restaurant> findAllByName(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return restaurantRepository.findAllByName(pageable);
    }

    public Page<Restaurant> findAllByName(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page - 1, size);
        if (search != null && !search.trim().isEmpty()) {
            return restaurantRepository.findByNameContainingIgnoreCase(search, pageable); // 이름 검색
        }
        return restaurantRepository.findAllByName(pageable);
    }

    public Page<Restaurant> findAllWithRatings(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page - 1, size);

        // 검색어가 존재하는 경우
        if (search != null && !search.trim().isEmpty()) {
            return restaurantRepository.findWithRatingsAndSearch(search, pageable); // 여기서 순서 확인
        }

        return restaurantRepository.findAllWithRatings(pageable); // 기본적으로 모든 레스토랑 가져오기
    }

}
