package org.ppocharong.muckstagram.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "menus")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(nullable = false)
    private String menuName;

    private Double price;
    private String description;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Ingredient> ingredients; // 메뉴에 포함된 재료 리스트

    // 재료 이름을 리스트로 반환하는 메서드
    public List<String> getIngredientNames() {
        return ingredients.stream()
                .map(Ingredient::getIngredientName)
                .collect(Collectors.toList());
    }
}
