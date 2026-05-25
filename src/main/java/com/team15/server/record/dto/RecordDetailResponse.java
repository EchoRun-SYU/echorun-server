package com.team15.server.record.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.team15.server.record.entity.Record;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RecordDetailResponse {
    private Long id;
    private double distance;
    private int duration;
    private LocalDateTime startTime;
    @JsonProperty("isCompleted")
    private boolean isCompleted;
    private String routeJson;
    private int trashCount;
    private String status;

    public RecordDetailResponse(Record record, int trashCount) {
        this.id = record.getId();
        this.distance = record.getDistance() != null ? record.getDistance() : 0.0;
        this.duration = record.getDuration() != null ? record.getDuration() : 0;
        this.startTime = record.getStartTime();
        this.isCompleted = Boolean.TRUE.equals(record.getIsCompleted());
        this.routeJson = record.getRouteJson();
        this.trashCount = trashCount;
        this.status = this.isCompleted ? "completed" : "in_progress";
    }
}
