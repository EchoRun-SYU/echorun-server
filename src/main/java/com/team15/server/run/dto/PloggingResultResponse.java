package com.team15.server.run.dto;

import com.team15.server.record.entity.Record;
import com.team15.server.trash.entity.TrashRecord;
import lombok.Getter;

@Getter
public class PloggingResultResponse {
    private Long runId;
    private double distance;
    private int duration;
    private int trashCount;

    // 탄소 관련 계산 필드
    private int runningCarbonSaving; // 달리기 절감량 (g)
    private int trashCarbonSaving;   // 수거 절감량 (g)
    private int totalCarbonSaving;   // 총 탄소 절감량 (g)
    private int treeAbsorption;      // 나무 흡수 환산 (일)

    public PloggingResultResponse(Record record, TrashRecord trashRecord) {
        this.runId = record.getId();
        // Record 엔티티의 distance가 Double 객체이므로 null 방어 처리
        this.distance = record.getDistance() != null ? record.getDistance() : 0.0;
        this.duration = record.getDuration() != null ? record.getDuration() : 0;

        // TrashRecord가 존재하면 개수를 가져오고, 없으면 0개 처리
        this.trashCount = trashRecord != null ? trashRecord.getTrashCount() : 0;

        // 달리기 절감: 거리(km) × 210g (반올림)
        this.runningCarbonSaving = (int) Math.round(this.distance * 210);

        // 수거 절감: 수거 개수 × 83g
        this.trashCarbonSaving = this.trashCount * 83;

        // 총 탄소 절감량 = 달리기 + 수거
        this.totalCarbonSaving = this.runningCarbonSaving + this.trashCarbonSaving;

        // 나무 흡수 환산: 총 절감량 ÷ 60g (반올림)
        this.treeAbsorption = (int) Math.round((double) this.totalCarbonSaving / 60);
    }
}