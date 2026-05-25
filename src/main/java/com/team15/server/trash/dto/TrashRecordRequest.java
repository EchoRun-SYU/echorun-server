package com.team15.server.trash.dto;

import lombok.Getter;

@Getter
public class TrashRecordRequest {
    private int trashCount;
    private String trashType;
    private String photoUrl;
}
