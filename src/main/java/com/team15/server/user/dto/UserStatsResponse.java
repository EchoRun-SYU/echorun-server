package com.team15.server.user.dto;

import com.team15.server.user.entity.User;
import lombok.Getter;

@Getter
public class UserStatsResponse {
    private double totalDistance;
    private int plogCount;
    private int totalExp;
    private int level;
    private int totalTrashCollected;

    public UserStatsResponse(User user, int totalTrashCollected) {
        this.totalDistance = user.getTotalDistance();
        this.plogCount = user.getPlogCount();
        this.totalExp = user.getExp();
        this.level = user.getLevel();
        this.totalTrashCollected = totalTrashCollected;
    }
}
