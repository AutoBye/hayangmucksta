package org.ppocharong.muckstagram.controller;

import jakarta.servlet.http.HttpSession;
import org.ppocharong.muckstagram.dto.MenuDto;
import org.ppocharong.muckstagram.dto.RestaurantDto;
import org.ppocharong.muckstagram.exception.ResourceNotFoundException;
import org.ppocharong.muckstagram.model.Menu;
import org.ppocharong.muckstagram.model.Restaurant;
import org.ppocharong.muckstagram.model.Review;
import org.ppocharong.muckstagram.model.User;
import org.ppocharong.muckstagram.repository.RestaurantRepository;
import org.ppocharong.muckstagram.service.RestaurantService;
import org.ppocharong.muckstagram.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/restaurants")  // 모든 요청을 /restaurants로 한정
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final ReviewService reviewService;
    public RestaurantController(RestaurantService restaurantService, ReviewService reviewService) {
        this.restaurantService = restaurantService;
        this.reviewService = reviewService;
    }

    @GetMapping
    public String showRestaurants(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "name") String sort,
                                  @RequestParam(required = false) String search,
                                  Model model, HttpSession session) {
        int pageSize = 10;
        Page<Restaurant> restaurantPage;

        if ("rating".equals(sort)) {
            restaurantPage = restaurantService.findAllWithRatings(page, pageSize, search); // 수정된 메서드 호출
        } else {
            restaurantPage = restaurantService.findAllByName(page, pageSize, search); // 가나다순 정렬
        }

        model.addAttribute("restaurants", restaurantPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", restaurantPage.getTotalPages());
        model.addAttribute("sort", sort);
        model.addAttribute("search", search); // 검색어 추가

        // 세션에서 사용자 정보 가져오기
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("username", user.getUsername());
        }

        return "restaurant-list"; // 음식점 목록 페이지 반환
    }

    //메뉴 확인. 아마 안쓸듯
    @GetMapping("/{id}/menus")
    public String getMenusByRestaurant(@PathVariable Long id, HttpSession session, Model model) {
        Restaurant restaurant = restaurantService.findRestaurantById(id);

        // 세션에서 사용자 정보 가져오기
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("username", user.getUsername());
        }

        if (restaurant == null) {
            throw new ResourceNotFoundException("Restaurant not found");
        }


        model.addAttribute("menus", restaurant.getMenus());
        return "menus";  // menus.html로 이동
    }

    //레스토랑 정보
    @GetMapping("/{restaurantId}")
    public String getRestaurantDetails(@PathVariable Long restaurantId, Model model, HttpSession session) {
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("reviews", restaurant.getReviews());

        // 세션에서 사용자 정보 가져오기
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);  // user 객체를 모델에 추가

        // 세션에서 사용자 정보 가져오기
        if (user != null) {
            model.addAttribute("username", user.getUsername());
        }

        // 평균 평점 계산 후 포맷팅
        double averageRating = reviewService.calculateAverageRating(restaurantId);
        String formattedAverageRating = String.format("%.1f", averageRating); // 소수점 1자리 포맷팅
        model.addAttribute("averageRating", formattedAverageRating);

        return "restaurant";
    }

    //메인에서 랜덤추천 버튼
    @GetMapping("/random")
    @ResponseBody
    public RestaurantDto getRandomRestaurant() {
        List<Restaurant> allRestaurants = restaurantService.getAllRestaurants();

        if (allRestaurants.isEmpty()) {
            throw new ResourceNotFoundException("No restaurants available");
        }

        // ThreadLocalRandom을 사용하여 안전하게 인덱스를 선택
        int randomIndex = ThreadLocalRandom.current().nextInt(allRestaurants.size());
        Restaurant selectedRestaurant = allRestaurants.get(randomIndex);

        // MenuDto 리스트 생성
        List<MenuDto> menuDtos = selectedRestaurant.getMenus().stream()
                .map(menu -> new MenuDto(
                        menu.getMenuName(),
                        Optional.ofNullable(menu.getPrice()).orElse(0.0),
                        Optional.ofNullable(menu.getDescription()).orElse("")))
                .toList();

        // RestaurantDto에 레스토랑 정보와 메뉴 정보 포함하여 반환
        return new RestaurantDto(
                selectedRestaurant.getName(),
                selectedRestaurant.getAddress(),
                menuDtos
        );
    }


}
