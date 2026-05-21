package com.team15.server.record.entity;

import com.team15.server.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "records")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 이게 바로 명세서의 runId가 됩니다!

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime startTime;

    // 종료할 때 채워넣을 필드들 (일단 null이나 0으로 시작)
    private Double distance;
    private Integer duration;
    private Boolean isCompleted;

    // 러닝 시작 시 사용할 생성자
    public Record(User user) {
        this.user = user;
        this.startTime = LocalDateTime.now();
        this.distance = 0.0;
        this.duration = 0;
        this.isCompleted = false;
    }
}