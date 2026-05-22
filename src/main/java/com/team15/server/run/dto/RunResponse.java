package com.team15.server.run.dto;

import com.team15.server.run.entity.Run;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RunResponse {
    private Long id;
    private double distance;
    private int duration;
    private double pace;
    private int calories;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private int trashCount;

    public RunResponse(Run run, int trashCount) {
        this.id = run.getId();
        this.distance = run.getDistance();
        this.duration = run.getDuration();
        this.pace = run.getPace();
        this.calories = run.getCalories();
        this.startTime = run.getStartTime();
        this.endTime = run.getEndTime();
        this.status = run.getStatus().name();
        this.trashCount = trashCount;
    }
}
