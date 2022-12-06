package com.example.tdd.service;

import com.example.tdd.domain.Membership;
import com.example.tdd.domain.MembershipRepository;
import com.example.tdd.domain.MembershipType;
import com.example.tdd.exception.MembershipErrorResult;
import com.example.tdd.exception.MembershipException;
import com.example.tdd.web.dto.MemberShipResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MembershipServiceTest {
    private final String userId = "userId";
    private final MembershipType membershipType = MembershipType.NAVER;
    private final Integer point = 10000;

    @InjectMocks
    private MembershipService target;

    @Mock
    private MembershipRepository membershipRepository;
    
    @Test
    void 멤버심등록실패_이미존재함(){
        // given
        doReturn(Membership.builder().build()).when(membershipRepository).findByUserIdAndMembershipType(userId, membershipType);

        // when
        final MembershipException result = assertThrows(MembershipException.class, () -> target.addMembership(userId, membershipType, point));

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.DUPLICATED_MEMERSHIP_REGISTER);
    }

    @Test
    void 멤버십등록성광(){
        // given
        doReturn(null).when(membershipRepository).findByUserIdAndMembershipType(userId, membershipType);
        doReturn(membership()).when(membershipRepository).save(any(Membership.class));

        // when
        final MemberShipResponse result = target.addMembership(userId, membershipType, point);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);

        // verify
        verify(membershipRepository, times(1)).findByUserIdAndMembershipType(userId, membershipType);
        verify(membershipRepository, times(1)).save(any(Membership.class));
    }
    public Membership membership(){
        return Membership.builder()
                .id(-1L)
                .userId(userId)
                .point(point)
                .membershipType(MembershipType.NAVER)
                .build();
    }
}