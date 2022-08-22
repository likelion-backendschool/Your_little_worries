package likelion.ylw;

import likelion.ylw.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemberServiceTests {
    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("회원가입이 가능하다.")
    public void t1() {
        memberService.create("member2", "member2@email.com", "1234", "haha", 100);
    }
}
