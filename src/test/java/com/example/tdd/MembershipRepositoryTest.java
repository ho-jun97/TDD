package com.example.tdd;

import com.example.tdd.domain.Membership;
import com.example.tdd.domain.MembershipRepository;
import com.example.tdd.domain.MembershipType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // JPA Repository들에 대한 빈들을 등록하여 단위 테스트의 작성을 용이하게 함
public class MembershipRepositoryTest {


    @Autowired
    private MembershipRepository membershipRepository;

    @Test
    public void MembershipRepository가Null아님() {
        assertThat(membershipRepository).isNotNull();
    }

    @Test
    public void 멤버심등록(){
        // given
        final Membership membership = Membership.builder()
                .userId("userId")
                .membershipName(MembershipType.NAVER)
                .point(10000)
                .build();
        // when

        final Membership result = membershipRepository.save(membership);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getUserId()).isEqualTo("userId");
        assertThat(result.getMembershipName()).isEqualTo(MembershipType.NAVER);
        assertThat(result.getPoint()).isEqualTo(10000);
    }
}
