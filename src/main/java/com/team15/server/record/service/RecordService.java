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
}