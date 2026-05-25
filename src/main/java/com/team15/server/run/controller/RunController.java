package com.team15.server.run.controller;

import com.team15.server.trash.dto.TrashRecordRequest;
import com.team15.server.trash.dto.TrashRecordResponse;
import com.team15.server.run.service.RunService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RunController {

    private final RunService runService;

    @PostMapping("/runs/{runId}/trash")
    public ResponseEntity<TrashRecordResponse> saveTrash(
            @PathVariable Long runId,
            @RequestParam Long userId,
            @RequestBody TrashRecordRequest request) {
        return ResponseEntity.ok(runService.saveTrash(runId, userId, request));
    }
}
