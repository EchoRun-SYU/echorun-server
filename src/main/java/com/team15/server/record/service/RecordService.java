package com.team15.server.record.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team15.server.record.dto.RecordSummaryResponse;
import com.team15.server.record.entity.Record;
import com.team15.server.record.repository.RecordRepository;
import com.team15.server.run.dto.PloggingResultResponse;
import com.team15.server.trash.entity.TrashRecord;
import com.team15.server.trash.repository.TrashRecordRepository;
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
@Transactional(readOnly = true)
public class RecordService {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final TrashRecordRepository trashRecordRepository;

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
                .map(record -> {
                    int trashCount = trashRecordRepository.findByRunId(record.getId()).stream()
                            .mapToInt(t -> t.getTrashCount())
                            .sum();
                    return new RecordSummaryResponse(record, trashCount);
                })
                .collect(Collectors.toList());
    }

    public PloggingResultResponse getPloggingResult(Long runId) {
        // 1. 러닝 기록(Record) 조회
        Record record = recordRepository.findById(runId)
                .orElseThrow(() -> new IllegalArgumentException("해당 플로깅 기록이 존재하지 않습니다. ID: " + runId));

        // 2. 해당 러닝에 쌓인 쓰레기 기록들을 List로 전부 가져옴
        List<TrashRecord> trashRecords = trashRecordRepository.findByRunId(runId);

        // 3. 리스트가 비어있지 않다면, 모든 TrashRecord의 trashCount를 더해줍니다.
        int totalTrashCount = 0;
        if (trashRecords != null && !trashRecords.isEmpty()) {
            totalTrashCount = trashRecords.stream()
                    .mapToInt(TrashRecord::getTrashCount)
                    .sum();
        }

        // 4. 가상의 TrashRecord 객체를 하나 만들어서 DTO에 넘겨주거나,
        // DTO가 바로 totalTrashCount를 받도록 깔끔하게 결합해서 반환합니다!

        // 임시로 합산된 쓰레기 개수를 가진 대표 TrashRecord 객체를 빌더로 만들어 전달합니다.
        TrashRecord dummyTrashRecord = TrashRecord.builder()
                .run(record)
                .user(record.getUser())
                .trashCount(totalTrashCount)
                .build();

        return new PloggingResultResponse(record, dummyTrashRecord);
    }
}