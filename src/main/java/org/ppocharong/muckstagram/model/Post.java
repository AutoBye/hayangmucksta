package org.ppocharong.muckstagram.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)  // 게시글과 사용자 간의 관계 설정
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 5000)  // 글 내용 최대 길이 설정 가능
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
