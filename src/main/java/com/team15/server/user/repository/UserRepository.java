package com.duyuthon.team15.server.user.repository;

import com.duyuthon.team15.server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일로 기존 가입 유저인지 확인하기 위한 메서드
    Optional<User> findByEmail(String email);
}