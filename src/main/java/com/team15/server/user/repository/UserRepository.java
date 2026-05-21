package com.team15.server.user.repository;

import com.team15.server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 구글 로그인 시 기존에 가입된 이메일인지 확인하기 위한 메서드
    Optional<User> findByEmail(String email);
}