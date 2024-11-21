package org.ppocharong.muckstagram.dto;

import java.util.List;

public class RestaurantDto {
    private String name;
    private String address;
    private List<MenuDto> menus;

    public RestaurantDto(String name, String address, List<MenuDto> menus) {
        this.name = name;
        this.address = address;
        this.menus = menus;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public List<MenuDto> getMenus() {
        return menus;
    }
}
