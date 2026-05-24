package com.team15.server.user.dto;

import com.team15.server.user.entity.User;
import lombok.Getter;

@Getter
public class RankingResponse {
    private int rank;
    private Long userId;
    private String nickname;
    private String region;
    private int level;
    private int exp;
    private double totalDistance;
    private int plogCount;

    public RankingResponse(int rank, User user) {
        this.rank = rank;
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.region = user.getRegion();
        this.level = user.getLevel();
        this.exp = user.getExp();
        this.totalDistance = user.getTotalDistance();
        this.plogCount = user.getPlogCount();
    }
}
