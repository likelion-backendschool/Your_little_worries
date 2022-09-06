package likelion.ylw.member;

import likelion.ylw.member.Mail.NotFoundEmailException;
import likelion.ylw.util.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

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
            throw new DataNotFoundException("아이디를 찾지 못했습니다.");
        }
    }

    public Member findByEmail(String email) {
        Optional<Member> om = this.memberRepository.findByEmail(email);
        if (om.isPresent()) {
            return om.get();
        }
        System.out.println("찾으려는 email: " + email);
        throw new NotFoundEmailException("해당 이메일에 가입된 정보가 없습니다.");
    }

    public void update(MemberUpdateForm memberUpdateForm, Member m) {
        Optional<Member> om = memberRepository.findByMemberId(m.getMemberId());
        if (om.isPresent()) {
            Member member = om.get();
            Optional<String> op = Optional.ofNullable(memberUpdateForm.getPassword1());
            System.out.println("---------------------");
            System.out.println("Optional password : " + op);
            System.out.println("---------------------");
            if(op.isPresent()) {
                System.out.println("---------------------");
                System.out.println("password 수정정보 존재");
                System.out.println("---------------------");
                member.setPassword(passwordEncoder.encode(memberUpdateForm.getPassword1()));
            }
            System.out.println("---------------------");
            System.out.println("memberUpdateForm.getNickname : " + memberUpdateForm.getNickname());
            System.out.println("---------------------");
            if(!memberUpdateForm.getNickname().equals("")) {
                System.out.println("---------------------");
                System.out.println("nickname은 공백이 아닙니다");
                System.out.println("---------------------");
                member.setNickname(memberUpdateForm.getNickname());
            }
            memberRepository.save(member);
            System.out.println("---------------------");
            System.out.println("회원정보를 수정했습니다");
            System.out.println("---------------------");
            return;
        }
        System.out.println("---------------------");
        System.out.println("회원정보를 수정하지 못했습니다");
        System.out.println("---------------------");
    }

    public void delete(MemberDeleteForm memberDeleteForm, String memberId) {
        Optional<Member> om = memberRepository.findByMemberId(memberId);
        if(om.isPresent()) {
            Member member = om.get();
            if (!passwordEncoder.matches(member.getPassword(), memberDeleteForm.getPassword())) {
                memberRepository.deleteById(member.getId());
                return;
            }
        }
    }
}

