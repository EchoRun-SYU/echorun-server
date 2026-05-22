package com.team15.server.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users") // SQL 예약어 'user'와의 충돌을 피하기 위해 테이블명은 보통 users로 지정합니다.
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email; // 구글에서 받아올 고유 이메일 (로그인 ID 대용)

    @Column(nullable = false)
    private String nickname;

    private String name;  // 구글 프로필 이름

    private String socialType;

    @Column(nullable = false)
    private int totalPoint; // 유저가 플로깅해서 얻은 총 점수 (초기값 0)

    private String region;

    @Column(nullable = false)
    private int exp = 0;

    @Column(nullable = false)
    private int level = 1;

    @Column(nullable = false)
    private double totalDistance = 0.0;

    @Column(nullable = false)
    private int plogCount = 0;

    @Builder
    public User(String email, String nickname, String socialType, String region) {
        this.email = email;
        this.nickname = nickname;
        this.socialType = socialType;
        this.region = region;
    }

    public void addExp(int amount) {
        this.exp += amount;
        this.level = calculateLevel(this.exp);
    }

    public void addDistance(double distance) {
        this.totalDistance += distance;
    }

    public void incrementPlogCount() {
        this.plogCount++;
    }

    private int calculateLevel(int exp) {
        if (exp < 100) return 1;
        if (exp < 250) return 2;
        if (exp < 450) return 3;
        if (exp < 700) return 4;
        if (exp < 1000) return 5;
        if (exp < 1400) return 6;
        if (exp < 1900) return 7;
        if (exp < 2500) return 8;
        if (exp < 3200) return 9;
        return 10;
    }
}