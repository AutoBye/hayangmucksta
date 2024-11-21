package org.ppocharong.muckstagram.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    private String phone;
    private String category;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>(); // Review와의 관계 추가

    @Transient // 이 어노테이션을 추가하여 JPA가 이 필드를 관리하지 않도록 합니다.
    private Double averageRating;

    public double getAverageRating() {
        // 평균 평점 계산
        return reviews.stream()
                .mapToDouble(Review::getRating) // Review에서 rating 속성 가져오기
                .average()
                .orElse(0.0); // 리뷰가 없으면 기본값 0.0
    }


    public double calculateAverageRating() {
        return reviews.stream()
                .mapToDouble(Review::getRating) // Review에서 rating 속성 가져오기
                .average()
                .orElse(0.0); // 리뷰가 없으면 기본값 0.0
    }


}
