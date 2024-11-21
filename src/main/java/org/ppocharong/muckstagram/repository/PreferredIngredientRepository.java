package org.ppocharong.muckstagram.repository;

import org.ppocharong.muckstagram.model.PreferredIngredient;
import org.ppocharong.muckstagram.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreferredIngredientRepository extends JpaRepository<PreferredIngredient, Long> {
    List<PreferredIngredient> findByUser_UserId(Long userId);
    void deleteByUser(User user);
}

