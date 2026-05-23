package com.team15.server.record.repository;

import com.team15.server.record.entity.Record;
import com.team15.server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findByUserOrderByStartTimeDesc(User user);
}