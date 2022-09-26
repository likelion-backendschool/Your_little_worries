package likelion.ylw.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    boolean existsByMemberId(String memberId);

    Optional<Member> findByMemberId(String memberId);

    Optional<Member> findByEmail(String email);

    List<Member> findAllByOrderByScoreDesc();

    List<Member> findTop20ByOrderByScoreDesc();
}