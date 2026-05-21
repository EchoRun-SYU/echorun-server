package com.team15.server.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 관련 API")
@RestController
public class AuthController {

    @Operation(summary = "구글 로그인 요청", description = "프론트에서 이 주소로 링크를 연결하면 구글 로그인 창이 뜹니다.")
    @GetMapping("/auth/google/login")
    public void googleLogin() {
        // 시큐리티가 가로채서 처리함
    }

    @Operation(summary = "구글 로그인 성공 리다이렉트 주소", description = "로그인 성공 시 토큰과 함께 이 주소로 리다이렉트 됩니다.")
    @GetMapping("/auth/google/success")
    public String loginSuccess(@RequestParam String token, @RequestParam String email) {
        return "구글 로그인 성공! \n이메일: " + email + "\n발급된 토큰: " + token;
    }
}