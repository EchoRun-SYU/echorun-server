package com.team15.server.security;

import com.team15.server.user.entity.User;
import com.team15.server.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = (String) oAuth2User.getAttributes().get("email");
        String name = (String) oAuth2User.getAttributes().get("name");

        // DB에 유저가 없으면 새로 회원가입(Save), 있으면 기존 유저 정보 가져오기
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .name(name)
                            .totalPoint(0) // 새 유저의 초기 점수는 0점
                            .build();
                    return userRepository.save(newUser);
                });

        // 유저 이메일 기반으로 암호화된 JWT 토큰 생성
        String realToken = jwtTokenProvider.createToken(user.getEmail());

        System.out.println("====== 🔥 [H2 DB 저장 검증] 가입된 유저 이메일: " + oAuth2User.getAttribute("email") + " ======");

        // 프론트엔드로 토큰과 이메일을 주소창에 실어서 던지기
        String targetUrl = UriComponentsBuilder.fromUriString("/auth/google/success")
                .queryParam("token", realToken)
                .queryParam("email", user.getEmail())
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}