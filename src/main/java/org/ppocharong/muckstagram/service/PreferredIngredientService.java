package org.ppocharong.muckstagram.service;

import org.ppocharong.muckstagram.model.PreferredIngredient;
import org.ppocharong.muckstagram.model.User;
import org.ppocharong.muckstagram.repository.IngredientRepository;
import org.ppocharong.muckstagram.repository.PreferredIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PreferredIngredientService {

    private final PreferredIngredientRepository preferredIngredientRepository;

    @Autowired
    public PreferredIngredientService(PreferredIngredientRepository preferredIngredientRepository) {
        this.preferredIngredientRepository = preferredIngredientRepository;
    }

    // 특정 사용자의 선호 재료 조회
    public List<String> getPreferredIngredientsByUserId(Long userId) {
        return preferredIngredientRepository.findByUser_UserId(userId)
                .stream()
                .map(PreferredIngredient::getIngredientName)  // 선호 재료의 이름만 반환
                .collect(Collectors.toList());
    }

    // 사용자의 선호 재료 목록을 반환하는 메서드
    public List<String> getPreferredIngredientsByUser(User user) {
        List<PreferredIngredient> preferredIngredients = preferredIngredientRepository.findByUser_UserId(user.getUserId());

        // PreferredIngredient 객체에서 재료 이름만 추출하여 반환
        return preferredIngredients.stream()
                .map(PreferredIngredient::getIngredientName)
                .collect(Collectors.toList());
    }

    // 선호 재료 저장
    public void savePreferredIngredients(User user, String[] ingredients) {
        Arrays.stream(ingredients)
                .map(String::trim)  // 공백 제거
                .filter(ingredient -> !ingredient.isEmpty())  // 빈 재료 제외
                .forEach(ingredient -> {
                    PreferredIngredient preferredIngredient = new PreferredIngredient();
                    preferredIngredient.setUser(user);
                    preferredIngredient.setIngredientName(ingredient);
                    preferredIngredientRepository.save(preferredIngredient);
                });
    }


    @Transactional
    public void updatePreferredIngredients(User user, List<PreferredIngredient> newIngredients) {
        // 사용자의 기존 선호 재료 삭제
        preferredIngredientRepository.deleteByUser(user);

        // 세션에 남아있는 데이터를 강제로 반영
        preferredIngredientRepository.flush();

        // 새로운 선호 재료 추가
        for (PreferredIngredient ingredient : newIngredients) {
            ingredient.setUser(user);
            preferredIngredientRepository.save(ingredient);
        }
    }
}
