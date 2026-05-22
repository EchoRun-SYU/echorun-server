package com.team15.server.trash.dto;

import com.team15.server.trash.entity.TrashRecord;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TrashRecordResponse {
    private Long id;
    private int trashCount;
    private String photoUrl;
    private int expGiven;
    private LocalDateTime createdAt;

    public TrashRecordResponse(TrashRecord record) {
        this.id = record.getId();
        this.trashCount = record.getTrashCount();
        this.photoUrl = record.getPhotoUrl();
        this.expGiven = record.getExpGiven();
        this.createdAt = record.getCreatedAt();
    }
}
