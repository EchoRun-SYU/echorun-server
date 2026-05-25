package com.team15.server.run.controller;

import com.team15.server.run.dto.RunResponse;
import com.team15.server.run.dto.RunSummaryResponse;
import com.team15.server.run.service.RunService;
import com.team15.server.trash.dto.TrashRecordRequest;
import com.team15.server.trash.dto.TrashRecordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RunController {

    private final RunService runService;

    @GetMapping("/runs")
    public ResponseEntity<List<RunSummaryResponse>> getRunList(@RequestParam Long userId) {
        return ResponseEntity.ok(runService.getRunList(userId));
    }

    @GetMapping("/runs/{runId}")
    public ResponseEntity<RunResponse> getRun(@PathVariable Long runId) {
        return ResponseEntity.ok(runService.getRun(runId));
    }

    @GetMapping("/runs")
    public ResponseEntity<List<RunSummaryResponse>> getRunList(@RequestParam Long userId) {
        return ResponseEntity.ok(runService.getRunList(userId));
    }

    @PostMapping("/runs/{runId}/trash")
    public ResponseEntity<TrashRecordResponse> saveTrash(
            @PathVariable Long runId,
            @RequestParam Long userId,
            @RequestBody TrashRecordRequest request) {
        return ResponseEntity.ok(runService.saveTrash(runId, userId, request));
    }
}
