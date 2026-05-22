package com.team15.server.trash.entity;

import com.team15.server.run.entity.Run;
import com.team15.server.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "trash_records")
public class TrashRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "run_id", nullable = false)
    private Run run;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int trashCount;

    private String photoUrl;

    private int expGiven;

    private LocalDateTime createdAt;

    @Builder
    public TrashRecord(Run run, User user, int trashCount, String photoUrl, int expGiven) {
        this.run = run;
        this.user = user;
        this.trashCount = trashCount;
        this.photoUrl = photoUrl;
        this.expGiven = expGiven;
        this.createdAt = LocalDateTime.now();
    }
}
