package com.example.tdd.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MembershipType {
    NAVER("네이버"),
    LINE("라인"),
    KAKAO("카카오"),
    ;
    public final  String companyName;

}
