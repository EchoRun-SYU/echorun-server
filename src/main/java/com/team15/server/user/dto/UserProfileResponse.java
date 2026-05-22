package com.team15.server.user.dto;

import com.team15.server.user.entity.User;
import lombok.Getter;

@Getter
public class UserProfileResponse {
    private Long id;
    private String email;
    private String nickname;
    private String region;
    private int level;
    private int exp;

    public UserProfileResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.region = user.getRegion();
        this.level = user.getLevel();
        this.exp = user.getExp();
    }
}
