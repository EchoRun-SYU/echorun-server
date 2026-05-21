package com.team15.server.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users") // SQL 예약어 'user'와 겹치지 않도록 테이블 이름은 users로 지정
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email; // 구글에서 받아올 고유 이메일 (로그인 ID 대용)

    @Column(nullable = false)
    private String name;  // 구글 프로필 이름

    @Column(nullable = false)
    private int totalPoint; // 유저가 플로깅해서 얻은 총 점수 (초기값 0)

    // 점수 업데이트가 필요할 때 쓸 메서드
    public void addPoints(int points) {
        this.totalPoint += points;
    }
}