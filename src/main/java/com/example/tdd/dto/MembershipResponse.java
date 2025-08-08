package com.example.tdd.dto;

import com.example.tdd.entity.MembershipType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MembershipResponse {
    private Long id;
    private MembershipType membershipType;
}
