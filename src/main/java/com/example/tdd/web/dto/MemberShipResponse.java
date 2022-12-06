package com.example.tdd.web.dto;

import com.example.tdd.domain.MembershipType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MemberShipResponse {

    private final Long id;
    private final MembershipType membershipType;
}
