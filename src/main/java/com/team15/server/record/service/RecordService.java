package com.team15.server.record.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team15.server.record.dto.RecordSummaryResponse;
import com.team15.server.record.entity.Record;
import com.team15.server.record.repository.RecordRepository;
import com.team15.server.user.entity.User;
import com.team15.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public Long startRun(String email) {
        // 1. 현재 로그인한 유저 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        // 2. 새로운 러닝 기록 생성 및 저장
        Record newRecord = new Record(user);
        Record savedRecord = recordRepository.save(newRecord);

        // 3. 생성된 러닝의 고유 ID(runId) 반환
        return savedRecord.getId();
    }

    @Transactional
    public void endRun(Long runId, Double distance, Integer duration, List<Map<String, Double>> route) {
        Record record = recordRepository.findById(runId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 러닝 기록입니다."));

        if (record.getIsCompleted()) {
            throw new IllegalStateException("이미 종료된 러닝 기록입니다.");
        }

        String routeJson = null;
        if (route != null && !route.isEmpty()) {
            try {
                routeJson = objectMapper.writeValueAsString(route);
            } catch (JsonProcessingException e) {
                routeJson = "[]";
            }
        }

        record.completeRun(distance, duration, routeJson);

        User user = record.getUser();
        user.addDistance(distance);
        user.addExp((int) (distance * 20));
        user.incrementPlogCount();
    }

    public List<RecordSummaryResponse> getRunList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        return recordRepository.findByUserOrderByStartTimeDesc(user).stream()
                .map(RecordSummaryResponse::new)
                .collect(Collectors.toList());
    }
}