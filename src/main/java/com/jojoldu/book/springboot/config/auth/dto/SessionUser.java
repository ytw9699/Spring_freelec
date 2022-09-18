package com.jojoldu.book.springboot.config.auth.dto;

import com.jojoldu.book.springboot.domain.user.User;
import lombok.Getter;

import java.io.Serializable;
// 세션에 사용자 정보를 저장하기 위한 dto 클래스
@Getter
public class SessionUser implements Serializable {//인증된 사용자정보만 필요
/*
Serializable를 이용해 직렬화 기능을 가진 세션 Dto를 하나 추가로 만드는것이 이후 운영 및 유지보수 때 많은 도움이 된다.
user entity는 언제 다른 엔티티와 관계가 형성될지 모르고, 예를 들어 @ OneToMany,@ManyToMany 등 자식 엔티티를 갖고 있다면
직렬화 대상에 자식들까지 포함되니 성능 이슈, 부수 효과가 발생할 확률이 높다.
*/
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
