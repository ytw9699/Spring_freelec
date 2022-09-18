package com.jojoldu.book.springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // JPA Auditing 활성화
public class JpaConfig {}

/*
@EnableJpaAuditing을 사용하기 위해선 최소 하나의 @Entity 클래스가 필요한데,
테스트를 위한 @WebMvcTest는 당연히 @Entity 클래스가 없다.
@EnableJpaAuditing을 @SpringBootApplication 와 함께 있다보니
@WebMvcTest에서도 스캔을 한다. 그래서 @EnableJpaAuditing과 @SpringBootApplication 둘을 분리하면
@WebMvcTest를 사용할수 있다.*/
