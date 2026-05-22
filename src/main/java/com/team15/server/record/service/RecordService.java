package com.team15.server.record.service;

import com.team15.server.record.entity.Record;
import com.team15.server.record.repository.RecordRepository;
import com.team15.server.user.entity.User;
import com.team15.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;

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
    public void endRun(Long runId, Double distance, Integer duration) {
        // 1. 해당 runId로 기존 러닝 기록 조회
        Record record = recordRepository.findById(runId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 러닝 기록입니다."));

        // 2. 만약 이미 종료된 러닝이라면 예외 처리
        if (record.getIsCompleted()) {
            throw new IllegalStateException("이미 종료된 러닝 기록입니다.");
        }

        // 3. 더티 체킹(Dirty Checking)으로 최종 데이터 업데이트
        // (엔티티에 업데이트용 메서드를 만들거나, 필드를 직접 수정할 수 있도록 Record.java에 Setter 또는 업데이트 메서드 추가 필요)
        record.completeRun(distance, duration);
    }
}