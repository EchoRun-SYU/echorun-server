package com.team15.server.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.team15.server.user.entity.User;
import com.team15.server.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@Tag(name = "Auth", description = "인증 관련 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Operation(
            summary = "플러터 네이티브 구글 토큰 교환 로그인",
            description = "프론트엔드(Flutter SDK)에서 발급받은 Google idToken을 Body에 실어 보내면, 검증 후 우리 서비스 전용 JWT 토큰을 반환합니다."
    )
    @PostMapping("/google/token")
    public ResponseEntity<?> loginWithGoogleToken(@RequestBody Map<String, String> request) {
        String idTokenString = request.get("idToken");

        if (idTokenString == null || idTokenString.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "idToken이 누락되었습니다."));
        }

        try {
            // 1. Google ID 토큰 검증기 생성
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            // 2. 토큰 검증 및 파싱
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                return ResponseEntity.status(401).body(Map.of("error", "유효하지 않은 Google ID 토큰입니다."));
            }

            // 3. 토큰에서 유저 이메일과 이름 추출
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String nicknameFromRequest = request.get("nickname");

            // 4. 기존 유저 조회
            java.util.Optional<User> existingUserOpt = userRepository.findByEmail(email);

            if (existingUserOpt.isEmpty()) {
                // 신규 유저 — 닉네임 없이 호출된 경우 닉네임 설정 화면으로 유도
                if (nicknameFromRequest == null || nicknameFromRequest.isBlank()) {
                    return ResponseEntity.ok(Map.of("isNewUser", true));
                }
                // 닉네임과 함께 호출된 경우 회원가입 처리
                User newUser = User.builder()
                        .email(email)
                        .name(name)
                        .nickname(nicknameFromRequest)
                        .totalPoint(0)
                        .region("SEOUL")
                        .level(1)
                        .exp(0)
                        .totalDistance(0.0)
                        .plogCount(0)
                        .build();
                User savedUser = userRepository.save(newUser);
                String appToken = jwtTokenProvider.createToken(savedUser.getEmail());
                return ResponseEntity.ok(Map.of(
                        "token", appToken,
                        "email", savedUser.getEmail(),
                        "userId", savedUser.getId(),
                        "isNewUser", true
                ));
            }

            // 기존 유저 로그인
            User user = existingUserOpt.get();

            // 5. 우리 서비스 전용 JWT 토큰 생성
            String appToken = jwtTokenProvider.createToken(user.getEmail());

            // 6. 결과 반환
            return ResponseEntity.ok(Map.of(
                    "token", appToken,
                    "email", user.getEmail(),
                    "userId", user.getId(),
                    "isNewUser", false
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "서버 검증 중 에러 발생: " + e.getMessage()));
        }
    }
}
