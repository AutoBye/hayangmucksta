package org.ppocharong.muckstagram.service;

import org.ppocharong.muckstagram.model.Menu;
import org.ppocharong.muckstagram.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    // 특정 식당의 메뉴 목록 조회
    public List<Menu> getMenusByRestaurantId(Long restaurantId) {
        return menuRepository.findByRestaurant_RestaurantId(restaurantId);
    }

    // 메뉴 저장
    public void saveMenu(Menu menu) {
        menuRepository.save(menu);
    }
}
