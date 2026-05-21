package com.team15.server.run.entity;

import com.team15.server.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "runs")
public class Run {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private double distance;

    private int duration;

    private double pace;

    private int calories;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private RunStatus status;

    @Builder
    public Run(User user, LocalDateTime startTime) {
        this.user = user;
        this.startTime = startTime;
        this.status = RunStatus.IN_PROGRESS;
    }

    public void end(double distance, int duration, double pace, int calories) {
        this.distance = distance;
        this.duration = duration;
        this.pace = pace;
        this.calories = calories;
        this.endTime = LocalDateTime.now();
        this.status = RunStatus.COMPLETED;
    }

    public void pause() {
        this.status = RunStatus.PAUSED;
    }

    public void resume() {
        this.status = RunStatus.IN_PROGRESS;
    }

    public enum RunStatus {
        IN_PROGRESS, PAUSED, COMPLETED
    }
}
