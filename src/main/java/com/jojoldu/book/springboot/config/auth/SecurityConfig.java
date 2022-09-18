package com.jojoldu.book.springboot.config.auth;

import com.jojoldu.book.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity//Spring Security 설정들을 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()//h2-console 호떤을 사용하기 위해 해당 옵션들을 disable
                .and()
                    .authorizeRequests()
                    //URL별 권한관리를 설정하는 옵션의 시작점. authorizeRequests 가 선언되어야만 antMatchers(권한관리 대상을 지정) 옵션 사용가능,URL, HTTP 메소드별로 관리가 가능
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile", "/v1").permitAll()//전체열람권한
                    //.antMatchers("/api/v1/**").hasRole(Role.USER.name())//USER 권한을 가진 사람만
                    .antMatchers("/api/v1/**").hasAnyRole(Role.GUEST.name(),Role.USER.name())
                    .anyRequest().authenticated()//설정된 값들 이외 나머지 url은 인증된 사용자(로그인한 사용자)만 허용
                .and()
                    .logout()
                        .logoutSuccessUrl("/")//로그아웃 성공 시 주소로 이동
                .and()
                    .oauth2Login()//OAuth 2 로그인 기능에 대한 여러 설정의 진입점
                        .userInfoEndpoint()//OAuth 2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당
                            .userService(customOAuth2UserService);//소셜 로그인 성공시 후처리 UseService 인터페이스의 구현체를 등록
                                            //리소스 서버(즉 소셜 서비스들) 에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시
    }
}