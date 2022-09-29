package likelion.ylw.member;

import likelion.ylw.member.mail.NotFoundEmailException;
import likelion.ylw.util.DataNotFoundException;
import likelion.ylw.util.Util;
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

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member create(String memberId, String password, String email, String nickname) {

        try {
            Member member = new Member();
            member.setMemberId(memberId);
            member.setPassword(passwordEncoder.encode(password));
            member.setEmail(email);
            member.setNickname(nickname);
            member.setRole(Role.USER);
//            memberService.setMemberImgByUrl(member, "https://i.imgur.com/64exPCm.png");
            memberRepository.save(member);
            return member;
        } catch (DataIntegrityViolationException e) {
            if (memberRepository.existsByMemberId(memberId)) {
                throw new SignupMemberIdDuplicatedException("이미 사용중인 아이디 입니다.");
            } else {
                throw new SignupEmailDuplicatedException("이미 사용중인 이메일 입니다.");
            }
        }
    }

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

    public void update(MemberUpdateForm memberUpdateForm, Member m) {
        Optional<Member> om = memberRepository.findByMemberId(m.getMemberId());

        if (om.isPresent()) {
            Member member = om.get();
            Optional<String> op = Optional.ofNullable(memberUpdateForm.getPassword1());

            if (op.isPresent()) {
                member.setPassword(passwordEncoder.encode(memberUpdateForm.getPassword1()));
            }
            if (!memberUpdateForm.getNickname().equals("")) {
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
        if (om.isPresent()) {
            Member member = om.get();

            if (passwordEncoder.matches(memberDeleteForm.getPassword(), member.getPassword())) {
                memberRepository.deleteById(member.getId());

                return;
            }

        }

    }

    public void updateImage(Member member, MultipartFile memberImg) {
//        String profileImgDirName = "member";
//        String fileName = UUID.randomUUID().toString() + ".png";
//        String profileImgDirPath = genFileDirPath + "/" + profileImgDirName;
//        String profileImgFilePath = profileImgDirPath + "/" + fileName;
//
//        new File(profileImgDirPath).mkdirs();
//        try {
//            memberImg.transferTo(new File(profileImgFilePath));
//            String profileImgRelPath = profileImgDirName + "/" + fileName;
//
//            member.setMemberImgPath(profileImgRelPath);
//            memberRepository.save(member);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }


        removeProfileImg(member);
        String profileImgRelPath = saveProfileImg(memberImg);

        member.setMemberImgPath(profileImgRelPath);
        memberRepository.save(member);
    }

    private String getCurrentProfileImgDirName() {

        return "member/" + Util.date.getCurrentDateFormatted("yyyy_MM_dd");
    }

//    public void setMemberImgByUrl(Member member, String url) {
//        String filePath = Util.file.downloadImg(url, genFileDirPath + "/" + getCurrentProfileImgDirName() + "/" + UUID.randomUUID());
//        member.setMemberImgPath(getCurrentProfileImgDirName() + "/" + new File(filePath).getName());
//        memberRepository.save(member);
//    }

    private String saveProfileImg(MultipartFile profileImg) {
        String profileImgDirName = getCurrentProfileImgDirName();

        String ext = Util.file.getExt(profileImg.getOriginalFilename());

        String fileName = UUID.randomUUID() + "." + ext;
        String profileImgDirPath = genFileDirPath + "/" + profileImgDirName;
        String profileImgFilePath = profileImgDirPath + "/" + fileName;

        new File(profileImgDirPath).mkdirs();

        try {
            profileImg.transferTo(new File(profileImgFilePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return profileImgDirName + "/" + fileName;
    }

    public void removeProfileImg(Member member) {
        member.removeProfileImgOnStorage();
        member.setMemberImgPath(null);
        memberRepository.save(member);
    }



    public List<Member> findAll() {
        return memberRepository.findAll();
    }


    /* 인기투표지:1000, 투표지:100, 투표하기:15 */
    // 투표지 등록 점수
    public void evalEnrollScore(Member member) {
        int count = member.getEnrollCount();
        int baseScore = 100;
        member.setEnrollScore(count * baseScore);
        memberRepository.save(member);
    }

    // 투표하기 점수
    public void evalParticipateScore(Member member) {
        int count = member.getParticipateCount();
        int baseScore = 15;
        member.setParticipateScore(count * baseScore);
        memberRepository.save(member);
    }

    // 인기투표지 선정
    public void evalPopularVoteScore(Member member) {
        int count = member.getPopularVoteCount();
        int baseScore = 1000;
        member.setPopularVoteScore(count * baseScore);
        memberRepository.save(member);
    }

    // 총점
    public void evalTotalScore(Member member) {
        int totalScore = member.getEnrollScore() + member.getParticipateScore() + member.getPopularVoteScore();
        member.setScore(totalScore);
        memberRepository.save(member);
    }

    public List<Member> getRankingList() {
        return memberRepository.findTop20ByOrderByScoreDesc();
    }

    public void evalRank() {
        int i = 1;
        List<Member> orderedMembers = memberRepository.findAllByOrderByScoreDesc();
        for (Member orderedMember : orderedMembers) {
            orderedMember.setCurrentRank(i);
            i++;
        }
    }

    public void evalLevel(Member member) {
        int score = member.getScore();
        if (score > 20000) {
            member.setCurrentLevel(7);
        } else if (score >= 12000) {
            member.setCurrentLevel(6);
        } else if (score >= 7000) {
            member.setCurrentLevel(5);
        } else if (score >= 3000) {
            member.setCurrentLevel(4);
        } else if (score >= 1000) {
            member.setCurrentLevel(3);
        } else if (score >= 500) {
            member.setCurrentLevel(2);
        } else {
            member.setCurrentLevel(1);
        }
        memberRepository.save(member);
    }
}
