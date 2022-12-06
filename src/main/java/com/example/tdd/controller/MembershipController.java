package com.example.tdd.controller;

import com.example.tdd.web.dto.MembershipRequest;
import com.example.tdd.web.dto.MembershipResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.example.tdd.constants.MembershipConstants.USER_ID_HEADER;


@RestController
public class MembershipController {

    @PostMapping("/api/v1/memberships")
    public ResponseEntity<MembershipResponse> addMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @RequestBody @Valid final MembershipRequest membershipRequest){

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
