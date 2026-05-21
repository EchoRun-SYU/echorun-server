package com.team15.server.security;

import com.team15.server.user.entity.User;
import com.team15.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@RequiredArgsConstructor // 의존성 주입(Lombok)을 위해 추가
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 구글이 넘겨준 유저 정보 가져오기
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String picture = (String) attributes.get("picture");

        System.out.println("구글 로그인 성공! 이메일: " + email + ", 이름: " + name);

        // 구글 정보로 DB 저장 및 업데이트 로직 실행
        User user = saveOrUpdate(email, name, picture);

        // 테스트용 콘솔 출력
        System.out.println("DB 저장 완료! 유저 ID: " + user.getId());

        return oAuth2User;
    }

    private User saveOrUpdate(String email, String name, String picture) {
        return userRepository.findByEmail(email)
                .map(entity -> {
                    return entity;
                })
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .name(name)
                            .build();
                    return userRepository.save(newUser);
                });
    }
}