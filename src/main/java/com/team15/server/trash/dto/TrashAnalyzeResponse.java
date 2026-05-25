package com.team15.server.trash.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "AI 쓰레기 분석 응답 데이터")
public class TrashAnalyzeResponse {

    @Schema(description = "인식된 쓰레기 종류", example = "PLASTIC")
    private String trashType;

    @Schema(description = "인식된 쓰레기 개수", example = "3")
    private Integer trashCount;

    @Schema(description = "획득한 환경 포인트", example = "50")
    private Integer pointsEarned;

    @Schema(description = "AI 분석 신뢰도 (0.0 ~ 1.0)", example = "0.92")
    private Double confidence;

    @Schema(description = "처리지침 및 코멘트", example = "페트병은 라벨을 제거하고 압착해서 버려주세요!")
    private String message;
}