package com.team15.server.run.repository;

import com.team15.server.run.entity.Run;
import com.team15.server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RunRepository extends JpaRepository<Run, Long> {
    List<Run> findByUserOrderByStartTimeDesc(User user);
}
