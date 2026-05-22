package com.team15.server.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "러닝 종료 요청 데이터")
public class RunEndRequest {

    @Schema(description = "최종 달린 거리 (km 단위)", example = "3.45")
    private Double distance;

    @Schema(description = "총 달린 시간 (초 단위)", example = "1200")
    private Integer duration;
}