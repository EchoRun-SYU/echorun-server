package com.team15.server.record.controller;

import com.team15.server.record.service.RecordService;
import com.team15.server.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/runs")
public class RecordController {

    private final RecordService recordService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startRun(HttpServletRequest request) {
        // 1. 헤더에서 토큰 추출 및 이메일 파싱
        String token = jwtTokenProvider.resolveToken(request);
        String email = jwtTokenProvider.getUserEmail(token);

        // 2. 러닝 시작 비즈니스 로직 실행
        Long runId = recordService.startRun(email);

        // 3. 명세서에 맞게 응답 데이터 구조 생성
        Map<String, Object> response = new HashMap<>();
        response.put("runId", runId);
        response.put("message", "러닝이 성공적으로 시작되었습니다.");

        return ResponseEntity.ok(response);
    }
}