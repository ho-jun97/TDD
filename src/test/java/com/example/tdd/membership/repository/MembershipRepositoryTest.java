package com.example.tdd.membership.repository;


import com.example.tdd.entity.Membership;
import com.example.tdd.entity.MembershipType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

// JPA Repository들에 대한 빈들을 등록하여 단위 테스트의 작성을 용이하게 함
// 테스트를 위해서 테스트 컨텍스트를 잡아주어야 하는데, @DataJpaTest는 내부적으로 @ExtendWith(SpringExtension.class) 가지고 있음
// @Transactional 도 가지고 있음
@DataJpaTest
public class MembershipRepositoryTest {

    @Autowired
    private MembershipRepository membershipRepository;

    @Test
    @DisplayName("MembershipRepository가 Null이 아님")
    public void nullTest() {
        assertThat(membershipRepository).isNotNull();
    }

    @Test
    @DisplayName("멤버십등록")
    public void membershipRegister() {
        // given
        final Membership membership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.NAVER)
                .point(10000)
                .build();
        // when
        final Membership result = membershipRepository.save(membership);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getUserId()).isEqualTo("userId");
        assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(result.getPoint()).isEqualTo(10000);

    }

    @Test
    @DisplayName("멤버십 존재하는지 테스트")
    public void membershipIsPresent() {
        // given
        final Membership membership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.NAVER)
                .point(10000)
                .build();

        // when
        membershipRepository.save(membership);
        final Membership result = membershipRepository.findByUserIdAndMembershipType("userId", MembershipType.NAVER);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getUserId()).isEqualTo("userId");
        assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(result.getPoint()).isEqualTo(10000);
    }

    @Test
    @DisplayName("멤버십 조회_사이즈 0")
    void membershipSizeZero() {
        // given

        // when
        List<Membership> result = membershipRepository.findAllByUserId("userId");

        // then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("멤버십 조회_사이즈 2")
    void membershipSizeTwo() {
        // given
        final Membership naverMembership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.NAVER)
                .point(10000)
                .build();

        final Membership kakaoMembership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.KAKAO)
                .point(10000)
                .build();

        membershipRepository.save(naverMembership);
        membershipRepository.save(kakaoMembership);

        // when
        List<Membership> result = membershipRepository.findAllByUserId("userId");

        // then

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("멤버십 추가 후 삭제")
    void addMembershipAfterDelete() {
        // given
        final Membership membership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.NAVER)
                .point(10000)
                .build();

        final Membership saveMembership = membershipRepository.save(membership);

        // when
        membershipRepository.deleteById(saveMembership.getId());

        // then
    }
}
