package com.duyuthon.team15.server.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users") // SQL 예약어 'user'와의 충돌을 피하기 위해 테이블명은 보통 users로 지정합니다.
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    // 소셜 로그인 타입 (KAKAO, NAVER 등)
    private String socialType;

    @Builder
    public User(String email, String name, String socialType) {
        this.email = email;
        this.name = name;
        this.socialType = socialType;
    }
}