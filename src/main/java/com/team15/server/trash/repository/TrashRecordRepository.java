package com.team15.server.trash.repository;

import com.team15.server.trash.entity.TrashRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrashRecordRepository extends JpaRepository<TrashRecord, Long> {
    List<TrashRecord> findByRunId(Long runId);
    List<TrashRecord> findByUserId(Long userId);
}
