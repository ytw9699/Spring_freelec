package com.jojoldu.book.springboot.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDTO {

    private String name;
    private String email;
    private String organization;

}