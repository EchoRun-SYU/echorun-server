package com.team15.server.record.dto;

import com.team15.server.record.entity.Record;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RecordSummaryResponse {
    private Long id;
    private double distance;
    private int duration;
    private LocalDateTime startTime;
    private boolean isCompleted;
    private String routeJson;

    public RecordSummaryResponse(Record record) {
        this.id = record.getId();
        this.distance = record.getDistance();
        this.duration = record.getDuration();
        this.startTime = record.getStartTime();
        this.isCompleted = record.getIsCompleted();
        this.routeJson = record.getRouteJson();
    }
}
