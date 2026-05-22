package com.team15.server.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nickname;

    private String socialType;

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
