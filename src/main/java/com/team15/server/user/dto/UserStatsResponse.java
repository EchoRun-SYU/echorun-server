package com.team15.server.user.dto;

import com.team15.server.user.entity.User;
import lombok.Getter;

@Getter
public class UserStatsResponse {
    private double totalDistance;
    private int plogCount;
    private int totalExp;
    private int level;

    public UserStatsResponse(User user) {
        this.totalDistance = user.getTotalDistance();
        this.plogCount = user.getPlogCount();
        this.totalExp = user.getExp();
        this.level = user.getLevel();
    }
}
