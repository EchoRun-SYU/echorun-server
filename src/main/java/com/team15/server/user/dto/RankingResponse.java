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
    private String levelTitle;
    private int exp;
    private double totalDistance;
    private int plogCount;

    public RankingResponse(int rank, User user) {
        this.rank = rank;
        this.userId = user.getId();
        this.nickname = user.getNickname();
        this.region = user.getRegion();
        this.level = user.getLevel();
        this.levelTitle = getLevelTitle(user.getLevel());
        this.exp = user.getExp();
        this.totalDistance = user.getTotalDistance();
        this.plogCount = user.getPlogCount();
    }

    private static String getLevelTitle(int level) {
        String[] titles = {"", "새싹 플로거", "초록 발걸음", "에코 런너", "환경 수호자",
                "그린 히어로", "플로깅 달인", "지구 지킴이", "에코 챔피언", "플로깅 레전드", "지구의 수호신"};
        return level < titles.length ? titles[level] : "지구의 수호신";
    }
}
