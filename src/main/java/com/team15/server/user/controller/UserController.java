package com.team15.server.user.controller;

import com.team15.server.user.dto.*;
import com.team15.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/me")
    public ResponseEntity<UserProfileResponse> getProfile(@RequestParam Long userId) {
        return ResponseEntity.ok(userService.getProfile(userId));
    }

    @GetMapping("/users/me/level")
    public ResponseEntity<UserLevelResponse> getLevel(@RequestParam Long userId) {
        return ResponseEntity.ok(userService.getLevel(userId));
    }

    @GetMapping("/users/me/stats")
    public ResponseEntity<UserStatsResponse> getStats(@RequestParam Long userId) {
        return ResponseEntity.ok(userService.getStats(userId));
    }

    @PostMapping("/exp")
    public ResponseEntity<ExpResponse> addExp(@RequestParam Long userId, @RequestBody ExpRequest request) {
        return ResponseEntity.ok(userService.addExp(userId, request));
    }

    @GetMapping("/rankings/global")
    public ResponseEntity<List<RankingResponse>> getGlobalRanking() {
        return ResponseEntity.ok(userService.getGlobalRanking());
    }

    @GetMapping("/rankings/region")
    public ResponseEntity<List<RankingResponse>> getRegionRanking(@RequestParam String region) {
        return ResponseEntity.ok(userService.getRegionRanking(region));
    }
}
