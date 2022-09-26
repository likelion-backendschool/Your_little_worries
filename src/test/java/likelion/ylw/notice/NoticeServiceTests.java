package likelion.ylw.notice;

import likelion.ylw.member.Member;
import likelion.ylw.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NoticeServiceTests {
    @Autowired
    private NoticeService noticeService;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("공지사항 생성")
    public void createNotice() {
        Member member = memberService.findByMemberId("member2");
        noticeService.create("제목입니다.", "내용입니다.", member);
    }

    @Test
    @DisplayName("공지사항 100개 생성")
    public void createNotice100() {
        Member member = memberService.findByMemberId("member2");
        for (int i = 0; i < 100; i++){
            noticeService.create(String.format("제목입니다%d", i), String.format("내용입니다%d", i), member);
        }
    }
}
