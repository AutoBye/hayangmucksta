package org.ppocharong.muckstagram.dto;

public class MenuDto {
    private String menuName;
    private double price;
    private String description;

    public MenuDto(String menuName, double price, String description) {
        this.menuName = menuName;
        this.price = price;
        this.description = description;
    }

    public String getMenuName() {
        return menuName;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }
}
