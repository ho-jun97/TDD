package com.example.tdd.dto;

import com.example.tdd.entity.MembershipType;
import com.example.tdd.membership.validation.ValidationGroups;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.tdd.membership.validation.ValidationGroups.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class MembershipRequest {
    @NotNull(groups = {MembershipAddMarker.class, MembershipAccumulateMarker.class})
    @Min(value=0, groups = {MembershipAddMarker.class, MembershipAccumulateMarker.class})
    private final Integer  point;

    @NotNull(groups = {MembershipAddMarker.class})
    private final MembershipType membershipType;
}
