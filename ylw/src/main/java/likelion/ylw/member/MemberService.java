package likelion.ylw.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member create(String memberId, String password, String email, String nickname, Integer score) {
        Member member = new Member();
        member.setMemberId(memberId);
        member.setPassword(passwordEncoder.encode(password));
        member.setEmail(email);
        member.setNickname(nickname);
        member.setScore(score);
        this.memberRepository.save(member);
        return member;
    }
}
