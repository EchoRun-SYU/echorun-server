package com.team15.server.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "러닝 종료 응답 데이터")
public class RunEndResponse {

    @Schema(description = "처리 결과 메시지", example = "러닝이 성공적으로 종료 및 기록되었습니다.")
    private String message;
}