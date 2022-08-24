package likelion.ylw.member;

import likelion.ylw.util.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member create(String memberId, String password, String email, String nickname) {
        Member member = new Member();
        member.setMemberId(memberId);
        member.setPassword(passwordEncoder.encode(password));
        member.setEmail(email);
        member.setNickname(nickname);
//        member.setScore(score);

        try {
            memberRepository.save(member);
        } catch (DataIntegrityViolationException e) {
            if (memberRepository.existsByMemberId(memberId)) {
                throw new SignupMemberIdDuplicatedException("이미 사용중인 아이디 입니다.");
            } else {
                throw new SignupEmailDuplicatedException("이미 사용중인 이메일 입니다.");
            }
        }
        return member;
    }


    /**
     * 유저아이디에 해당하는 유저 객체 가져오기
     */
    public Member getMemberId(String memberId) {
        Optional<Member> member = this.memberRepository.findByMemberId(memberId);
        if (member.isPresent()) {
            return member.get();
        } else {
            throw new DataNotFoundException("memberId not found");
        }
    }
}
