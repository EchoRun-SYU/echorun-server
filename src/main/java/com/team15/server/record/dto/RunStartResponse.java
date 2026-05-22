package com.team15.server.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "러닝 시작 응답 데이터")
public class RunStartResponse {

    @Schema(description = "새로 생성된 러닝 세션의 고유 ID(Run ID)", example = "42")
    private Long runId;

    @Schema(description = "처리 결과 메시지", example = "러닝이 성공적으로 시작되었습니다.")
    private String message;
}