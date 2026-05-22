package com.team15.server.user.service;

import com.team15.server.user.dto.*;
import com.team15.server.user.entity.User;
import com.team15.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserProfileResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        return new UserProfileResponse(user);
    }

    public UserLevelResponse getLevel(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        return new UserLevelResponse(user);
    }

    public UserStatsResponse getStats(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        return new UserStatsResponse(user);
    }

    @Transactional
    public ExpResponse addExp(Long userId, ExpRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        int prevLevel = user.getLevel();
        user.addExp(request.getAmount());
        boolean leveledUp = user.getLevel() > prevLevel;
        return new ExpResponse(user, request.getAmount(), leveledUp);
    }

    public List<RankingResponse> getGlobalRanking() {
        List<User> users = userRepository.findAllByOrderByExpDesc();
        return IntStream.range(0, users.size())
                .mapToObj(i -> new RankingResponse(i + 1, users.get(i)))
                .collect(Collectors.toList());
    }

    public List<RankingResponse> getRegionRanking(String region) {
        List<User> users = userRepository.findByRegionOrderByExpDesc(region);
        return IntStream.range(0, users.size())
                .mapToObj(i -> new RankingResponse(i + 1, users.get(i)))
                .collect(Collectors.toList());
    }
}
