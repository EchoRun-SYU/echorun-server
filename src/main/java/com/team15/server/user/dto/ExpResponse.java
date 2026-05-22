package com.team15.server.user.dto;

import com.team15.server.user.entity.User;
import lombok.Getter;

@Getter
public class ExpResponse {
    private int addedExp;
    private int totalExp;
    private int level;
    private boolean leveledUp;

    public ExpResponse(User user, int addedExp, boolean leveledUp) {
        this.addedExp = addedExp;
        this.totalExp = user.getExp();
        this.level = user.getLevel();
        this.leveledUp = leveledUp;
    }
}
