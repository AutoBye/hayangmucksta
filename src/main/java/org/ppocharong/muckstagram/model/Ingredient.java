package org.ppocharong.muckstagram.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ingredientId;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column(nullable = false)
    private String ingredientName;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

}
