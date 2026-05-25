package com.team15.server.run.service;

import com.team15.server.record.entity.Record;
import com.team15.server.record.repository.RecordRepository;
import com.team15.server.run.dto.RunResponse;
import com.team15.server.run.dto.RunSummaryResponse;
import com.team15.server.run.entity.Run;
import com.team15.server.run.repository.RunRepository;
import com.team15.server.trash.dto.TrashRecordRequest;
import com.team15.server.trash.dto.TrashRecordResponse;
import com.team15.server.trash.entity.TrashRecord;
import com.team15.server.trash.repository.TrashRecordRepository;
import com.team15.server.user.entity.User;
import com.team15.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RunService {

    private final RunRepository runRepository;
    private final RecordRepository recordRepository;
    private final TrashRecordRepository trashRecordRepository;
    private final UserRepository userRepository;

    public RunResponse getRun(Long runId) {
        Run run = runRepository.findById(runId)
                .orElseThrow(() -> new IllegalArgumentException("러닝 기록을 찾을 수 없습니다."));
        int trashCount = trashRecordRepository.findByRunId(runId).size();
        return new RunResponse(run, trashCount);
    }

    public List<RunSummaryResponse> getRunList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        return runRepository.findByUserOrderByStartTimeDesc(user).stream()
                .map(RunSummaryResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public TrashRecordResponse saveTrash(Long runId, Long userId, TrashRecordRequest request) {
        Record run = recordRepository.findById(runId)
                .orElseThrow(() -> new IllegalArgumentException("러닝 기록을 찾을 수 없습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        int expGiven = request.getTrashCount() * 10;
        TrashRecord record = TrashRecord.builder()
                .run(run)
                .user(user)
                .trashCount(request.getTrashCount())
                .trashType(request.getTrashType())
                .photoUrl(request.getPhotoUrl())
                .expGiven(expGiven)
                .build();

        user.addExp(expGiven);
        user.incrementPlogCount();

        return new TrashRecordResponse(trashRecordRepository.save(record));
    }
}
