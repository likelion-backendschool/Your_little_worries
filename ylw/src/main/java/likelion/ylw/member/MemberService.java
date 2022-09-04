package likelion.ylw.member;

import likelion.ylw.member.Mail.NotFoundEmailException;
import likelion.ylw.util.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public Member findByEmail(String email) {
        Optional<Member> om = this.memberRepository.findByEmail(email);
        if (!om.isPresent()) {
            System.out.println("찾으려는 email: " + email);
        }
        Member member = om.get();
        return member;
    }

    public List<Member> findAll() {
        List<Member> members = this.memberRepository.findAll();
        if(members.isEmpty()) {
            throw new NotFoundEmailException("입력하신 이메일로 가입된 정보가 없습니다.");
        }
        return members;
    }
}
