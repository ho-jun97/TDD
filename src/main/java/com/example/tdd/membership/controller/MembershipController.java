package com.example.tdd.membership.controller;

import com.example.tdd.dto.MembershipDetailResponse;
import com.example.tdd.dto.MembershipRequest;
import com.example.tdd.dto.MembershipAddResponse;
import com.example.tdd.membership.service.MembershipService;
import com.example.tdd.membership.validation.ValidationGroups;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.tdd.constants.MembershipConstants.USER_ID_HEADER;
import static com.example.tdd.membership.validation.ValidationGroups.*;

@RestController
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @PostMapping("/api/v1/memberships")
    public ResponseEntity<MembershipAddResponse> addMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @RequestBody @Validated(MembershipAddMarker.class) final MembershipRequest membershipRequest) {

        final MembershipAddResponse membershipAddResponse = membershipService.addMembership(userId, membershipRequest.getMembershipType(), membershipRequest.getPoint());

        return ResponseEntity.status(HttpStatus.CREATED).body(membershipAddResponse);
    }

    @GetMapping("/api/v1/memberships")
    public ResponseEntity<List<MembershipDetailResponse>> deleteMembership(
            @RequestHeader(USER_ID_HEADER) final String userId
    ) {
        return ResponseEntity.ok(membershipService.getMembershipList(userId));
    }

    @GetMapping("/api/v1/memberships/{membershipId}")
    public ResponseEntity<MembershipDetailResponse> getMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @PathVariable final Long membershipId) {
        return ResponseEntity.ok(membershipService.getMembership(membershipId, userId));
    }

    @DeleteMapping("/api/v1/memberships/{membershipId}")
    public ResponseEntity<Void> deleteMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @PathVariable final Long membershipId
    ){
        membershipService.getMembership(membershipId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/v1/memberships/{membershipId}/accumulate")
    public ResponseEntity<Void> accumulateMembershipPoint(
        @RequestHeader(USER_ID_HEADER) final String userId,
        @PathVariable final Long membershipId,
        @RequestBody @Validated(MembershipAccumulateMarker.class) final MembershipRequest membershipRequest
    ) {
        membershipService.accumulateMembershipPoint(membershipId, userId, membershipRequest.getPoint());
        return ResponseEntity.noContent().build();
    }

}
