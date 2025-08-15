package com.example.tdd.membership.service;


import com.example.tdd.dto.MembershipDetailResponse;
import com.example.tdd.entity.Membership;
import com.example.tdd.dto.MembershipAddResponse;
import com.example.tdd.entity.MembershipType;
import com.example.tdd.exception.MembershipErrorResult;
import com.example.tdd.exception.MembershipException;
import com.example.tdd.membership.repository.MembershipRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MembershipServiceTest {

    private final String userId = "userId";
    private final MembershipType membershipType = MembershipType.NAVER;
    private final Integer point = 10000;
    private final Long membershipId = -1L;

    @InjectMocks
    private MembershipService target;

    @Mock
    private MembershipRepository membershipRepository;

    @Test
    @DisplayName("멤버십등록실패_이미존재함")
    public void alreadyMembership() {
        // given
        doReturn(Membership.builder().build())
                .when(membershipRepository).findByUserIdAndMembershipType(userId, membershipType);

        // when
        final MembershipException result = assertThrows(MembershipException.class, () -> target.addMembership(userId, membershipType, point));

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
    }

    @Test
    @DisplayName("멤버십등록성공")
    public void membershipRegisterSuccess() {
        // given
        doReturn(null).when(membershipRepository).findByUserIdAndMembershipType(userId, membershipType);
        doReturn(membership()).when(membershipRepository).save(any(Membership.class));

        // when
        final MembershipAddResponse result = target.addMembership(userId, membershipType, point);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);

        // verify
        verify(membershipRepository, times(1)).findByUserIdAndMembershipType(userId, membershipType);
        verify(membershipRepository, times(1)).save(any(Membership.class));
    }
    private Membership membership() {
        return Membership.builder()
                .id(-1L)
                .userId(userId)
                .point(point)
                .membershipType(MembershipType.NAVER)
                .build();
    }
    @Test
    @DisplayName("멤버십목록조회")
    void getMembershipList() {
        // given
        doReturn(Arrays.asList(
                Membership.builder().build(),
                Membership.builder().build(),
                Membership.builder().build()
        )).when(membershipRepository).findAllByUserId(userId);

        // when
        final List<MembershipDetailResponse> result = target.getMembershipList(userId);

        // then
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("멤버십상세조회실패_존재하지않음")
    void getMembershipDetailFailNotExist() {
        // given
        doReturn(Optional.empty())
                .when(membershipRepository).findById(membershipId);

        // when
        final MembershipException result = assertThrows(MembershipException.class, () -> target.getMembership(membershipId, userId));

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
    }
    @Test
    @DisplayName("멤버십상세조회실패_본인이아님")
    void getMembershipDetailFailByUserId() {
        // given
        doReturn(Optional.empty())
                .when(membershipRepository).findById(membershipId);

        // when
        final MembershipException result = assertThrows(MembershipException.class, () ->target.getMembership(membershipId, "notonwer"));

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
    }

    @Test
    @DisplayName("멤버십상세조회성공")
    void getMembershipDetailSuccess() {
        // given
        doReturn(Optional.of(membership())).when(membershipRepository).findById(membershipId);

        // when
        final MembershipDetailResponse result = target.getMembership(membershipId, userId);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(result.getPoint()).isEqualTo(point);
    }

    @Test
    @DisplayName("멤버십삭제실패 존재하지 않음")
    void membershipDeleteFailNotExist() {
        // given
        doReturn(Optional.empty())
                .when(membershipRepository).findById(membershipId);

        // when
        final MembershipException result = assertThrows(MembershipException.class,  () -> target.removeMembership(membershipId, userId));

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
    }

    @Test
    @DisplayName("멤버십삭제실패_본인이아님")
    void membershipDeleteFailByNotMine() {
        // given
        final Membership membership = membership();
        doReturn(Optional.of(membership)).when(membershipRepository).findById(membershipId);

        // when
        final MembershipException result = assertThrows(MembershipException.class, ()->target.removeMembership(membershipId, "notonwer"));

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
    }

    @Test
    @DisplayName("멤버십삭제성공")
    void memebershipDeleteSuccess() {
        // given
        final Membership membership = membership();
        doReturn(Optional.of(membership)).when(membershipRepository).findById(membershipId);

        // when
        target.removeMembership(membershipId, userId);

    }
}
