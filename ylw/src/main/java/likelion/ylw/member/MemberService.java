package likelion.ylw.member;

import likelion.ylw.member.mail.NotFoundEmailException;
import likelion.ylw.util.DataNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MemberService {
    @Value("${custom.genFileDirPath}")
    private String genFileDirPath;
    String profileImgRelPath;
    File profileImgFile;


    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public Member create(String memberId, String password, String email, String nickname, MultipartFile memberImg) {
        profileImgRelPath = "member/default.png";
        profileImgFile = new File(genFileDirPath + "/" + profileImgRelPath);

        profileImgFile.mkdirs();

        try {
            memberImg.transferTo(profileImgFile);
            System.out.println("-----------------------");
            System.out.println("MultipartFile memberImg: " + memberImg);
            System.out.println("-----------------------");
            System.out.println("String profileImgRelPath: " + profileImgRelPath);
            System.out.println("-----------------------");
            Member member = new Member();
            member.setMemberId(memberId);
            member.setPassword(passwordEncoder.encode(password));
            member.setEmail(email);
            member.setNickname(nickname);
            member.setMemberImgPath(profileImgRelPath);
            memberRepository.save(member);
            return member;
        } catch (DataIntegrityViolationException e) {
            if (memberRepository.existsByMemberId(memberId)) {
                throw new SignupMemberIdDuplicatedException("이미 사용중인 아이디 입니다.");
            } else {
                throw new SignupEmailDuplicatedException("이미 사용중인 이메일 입니다.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 유저아이디에 해당하는 유저 객체 가져오기
     */
    public Member findByMemberId(String memberId) {
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

    public void update(MemberUpdateForm memberUpdateForm, Member m, MultipartFile memberImg) {
        Optional<Member> om = memberRepository.findByMemberId(m.getMemberId());
        profileImgRelPath = "member/" + UUID.randomUUID().toString()+".png";
        profileImgFile = new File(genFileDirPath + "/" + profileImgRelPath);

        if (om.isPresent()) {
            Member member = om.get();
            Optional<String> op = Optional.ofNullable(memberUpdateForm.getPassword1());

            if(op.isPresent()) {
                member.setPassword(passwordEncoder.encode(memberUpdateForm.getPassword1()));
            }
            if(!memberUpdateForm.getNickname().equals("")) {
                member.setNickname(memberUpdateForm.getNickname());
            }
            System.out.println("--------------------------");
            System.out.println("이미지 변경 안했을 시 값 : " + memberUpdateForm.getMemberImg());
            System.out.println("--------------------------");

            memberRepository.save(member);

            return;
        }

    }

    public void delete(MemberDeleteForm memberDeleteForm, String memberId) {
        Optional<Member> om = memberRepository.findByMemberId(memberId);
        if(om.isPresent()) {
            Member member = om.get();

            if (passwordEncoder.matches(memberDeleteForm.getPassword(), member.getPassword())) {
                memberRepository.deleteById(member.getId());

                return;
            }

        }

    }

    public void updateImage(Member member, MultipartFile memberImg) {
        profileImgRelPath = "member" + UUID.randomUUID().toString() + ".png";
        profileImgFile = new File(genFileDirPath + "/" + profileImgRelPath);

        profileImgFile.mkdirs();
        try {
            memberImg.transferTo(profileImgFile);
            member.setMemberImgPath(profileImgRelPath);
            memberRepository.save(member);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }
}

