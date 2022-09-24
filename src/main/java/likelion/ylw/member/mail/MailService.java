package likelion.ylw.member.mail;

import likelion.ylw.member.Member;
import likelion.ylw.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void reset(Member member, String password) {
        member.setPassword(passwordEncoder.encode(password));
        memberRepository.save(member);
    }
}
