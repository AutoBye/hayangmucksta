package org.ppocharong.muckstagram.repository;

import org.ppocharong.muckstagram.model.Ingredient;
import org.ppocharong.muckstagram.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
