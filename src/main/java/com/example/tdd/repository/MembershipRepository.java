package com.example.tdd.repository;

import com.example.tdd.entity.Membership;
import com.example.tdd.entity.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;

// @Respository를 붙어주어야 정상이지만 JpaRepsoitory 하위에 @Repository가 붙어있으므로 붙어주지 않아도 됨
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Membership findByUserIdAndMembershipType(String userId, MembershipType membershipType);
}
