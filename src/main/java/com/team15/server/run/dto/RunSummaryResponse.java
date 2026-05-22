package com.team15.server.run.dto;

import com.team15.server.run.entity.Run;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RunSummaryResponse {
    private Long id;
    private double distance;
    private int duration;
    private double pace;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public RunSummaryResponse(Run run) {
        this.id = run.getId();
        this.distance = run.getDistance();
        this.duration = run.getDuration();
        this.pace = run.getPace();
        this.startTime = run.getStartTime();
        this.endTime = run.getEndTime();
    }
}
