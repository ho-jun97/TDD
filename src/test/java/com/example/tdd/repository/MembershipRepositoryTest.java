package com.example.tdd.repository;


import com.example.tdd.entity.Membership;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
                .membershipName("네이버")
                .point(10000)
                .build();
        // when
        final Membership result = membershipRepository.save(membership);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getUserId()).isEqualTo("userId");
        assertThat(result.getMembershipName()).isEqualTo("네이버");
        assertThat(result.getPoint()).isEqualTo(10000);

    }
}
