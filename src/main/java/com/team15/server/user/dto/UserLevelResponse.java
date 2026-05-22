package com.team15.server.user.dto;

import com.team15.server.user.entity.User;
import lombok.Getter;

@Getter
public class UserLevelResponse {
    private int level;
    private String levelTitle;
    private int currentExp;
    private int nextLevelExp;

    public UserLevelResponse(User user) {
        this.level = user.getLevel();
        this.currentExp = user.getExp();
        this.levelTitle = getLevelTitle(user.getLevel());
        this.nextLevelExp = getNextLevelExp(user.getLevel());
    }

    private String getLevelTitle(int level) {
        String[] titles = {"", "새싹 플로거", "초록 발걸음", "에코 런너", "환경 수호자",
                "그린 히어로", "플로깅 달인", "지구 지킴이", "에코 챔피언", "플로깅 레전드", "지구의 수호신"};
        return level < titles.length ? titles[level] : "지구의 수호신";
    }

    private int getNextLevelExp(int level) {
        int[] thresholds = {100, 250, 450, 700, 1000, 1400, 1900, 2500, 3200, Integer.MAX_VALUE};
        return level <= thresholds.length ? thresholds[level - 1] : Integer.MAX_VALUE;
    }
}
