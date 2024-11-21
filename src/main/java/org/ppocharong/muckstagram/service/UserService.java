package org.ppocharong.muckstagram.service;

import org.ppocharong.muckstagram.model.User;
import org.ppocharong.muckstagram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 회원 저장
    public void saveUser(User user) throws IllegalArgumentException {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이름입니다.");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("해당 이메일은 이미 가입되었습니다.");
        }
        userRepository.save(user);
    }

    // 사용자 ID로 사용자 찾기
    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);  // ID로 사용자 조회
    }

    // 사용자명으로 사용자 찾기
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // 이메일로 사용자 찾기
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean checkPassword(User user, String rawPassword) {
        // 비밀번호는 실제로는 암호화 후 비교하는 것이 안전합니다.
        return user.getPassword().equals(rawPassword);
    }
}
