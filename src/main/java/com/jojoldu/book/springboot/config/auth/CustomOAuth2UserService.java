package com.jojoldu.book.springboot.config.auth;

import com.jojoldu.book.springboot.config.auth.dto.OAuthAttributes;
import com.jojoldu.book.springboot.config.auth.dto.SessionUser;
import com.jojoldu.book.springboot.domain.user.User;
import com.jojoldu.book.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    // 이 클래스는 구글 로그인 이후 가져온 사용자의 정보(email ,name, picture 등)들을 기반으로 가입 및 정보수정, 세션 저장 등의 기능을 지원

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();//로그인 진행 증인 서비스를 구분하는 코드 ex: 구글 or 네이버
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                                                  .getUserInfoEndpoint().getUserNameAttributeName();
        //userNameAttributeName는 0Auth2 로그인 진행 시 키가 되는 필드값을 의미, Primary Key와 같은 의미
        //구글의 경우 기본적으로 코드를 지원하지만, 네이버 카카오 등은 기본 지원하지 않는다 구글의 기본 코드는 "sub"이다.이후 네이버 로그인과 구글로그인을 동시지원할때 사용

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        //0Auth2UserService 를 통해 가져온 0Auth2User의 attribute 를 담을 클래스
        //이후 네이버 등 다른 소셜 로그인도 이 클래스를 사용

        User user = saveOrUpdate(attributes);//구글 사용자 정보가 업데이트 되었 을때 를 대비
        httpSession.setAttribute("user", new SessionUser(user));//로그인 성공시 세션에 SessionUser 저장

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }


    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
