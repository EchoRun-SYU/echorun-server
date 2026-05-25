package com.team15.server.record.controller;

import com.team15.server.record.dto.RecordDetailResponse;
import com.team15.server.record.dto.RecordSummaryResponse;
import com.team15.server.record.dto.RunEndRequest;
import com.team15.server.record.dto.RunEndResponse;
import com.team15.server.record.dto.RunStartResponse;
import com.team15.server.record.service.RecordService;
import com.team15.server.run.dto.PloggingResultResponse;
import com.team15.server.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/runs")
@Tag(name = "🏃‍♂️ Running API", description = "러닝 시작 및 종료 등 기록 관리를 위한 API입니다.")
public class RecordController {

    private final RecordService recordService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/start")
    @Operation(summary = "러닝 시작 API", description = "헤더의 JWT 토큰을 기반으로 새로운 러닝 세션을 생성하고 runId를 발급합니다.")
    public ResponseEntity<RunStartResponse> startRun(HttpServletRequest request) {
        // 1. 헤더에서 토큰 추출 및 이메일 파싱
        String token = jwtTokenProvider.resolveToken(request);
        String email = jwtTokenProvider.getUserEmail(token);

        // 2. 비즈니스 로직 실행 및 ID 반환
        Long runId = recordService.startRun(email);

        // 3. 정석 DTO 응답 리턴 (스웨거에 자동으로 반영)
        return ResponseEntity.ok(new RunStartResponse(runId, "러닝이 성공적으로 시작되었습니다."));
    }

    @PostMapping("/{runId}/end")
    @Operation(summary = "러닝 종료 API", description = "프론트엔드에서 측정한 최종 거리(km)와 시간(초)을 받아 기록을 마감합니다.")
    public ResponseEntity<RunEndResponse> endRun(
            @PathVariable Long runId,
            @RequestBody RunEndRequest runEndRequest) {

        recordService.endRun(runId, runEndRequest.getDistance(), runEndRequest.getDuration(), runEndRequest.getRoute());
        return ResponseEntity.ok(new RunEndResponse("러닝이 성공적으로 종료 및 기록되었습니다."));
    }

    @GetMapping
    @Operation(summary = "내 러닝 기록 목록 조회", description = "userId로 해당 유저의 완료된 러닝 기록 목록을 조회합니다.")
    public ResponseEntity<List<RecordSummaryResponse>> getRunList(@RequestParam Long userId) {
        return ResponseEntity.ok(recordService.getRunList(userId));
    }

    @GetMapping("/{runId}")
    @Operation(summary = "러닝 기록 상세 조회", description = "runId로 특정 러닝 기록의 상세 정보를 조회합니다.")
    public ResponseEntity<RecordDetailResponse> getRun(@PathVariable Long runId) {
        return ResponseEntity.ok(recordService.getRun(runId));
    }

    // 컨트롤러 파일에 추가할 메서드

    @Operation(summary = "플로깅 종료 후 탄소 절감 결과 상세 조회", description = "특정 runId(Record ID)를 기반으로 거리와 쓰레기 수거 데이터를 취합하여 탄소 절감량 계산 결과를 반환합니다.")
    @GetMapping("/{runId}/result")
    public ResponseEntity<PloggingResultResponse> getPloggingResult(@PathVariable Long runId) {
        // 방금 RecordService에 만든 메서드 호출!
        PloggingResultResponse response = recordService.getPloggingResult(runId);
        return ResponseEntity.ok(response);
    }
}