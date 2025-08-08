package com.example.tdd.dto;

import com.example.tdd.entity.MembershipType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class MembershipRequest {
    @NotNull
    @Min(0)
    private final Integer  point;

    @NotNull
    private final MembershipType membershipType;
}
