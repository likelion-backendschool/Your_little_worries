package likelion.ylw.comment;

import javassist.bytecode.annotation.MemberValue;
import likelion.ylw.article.Article;
import likelion.ylw.article.ArticleRepository;
import likelion.ylw.member.Member;
import likelion.ylw.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CommentServiceTest {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private CommentService commentService;


    @Transactional
    @DisplayName("댓글을 DB에 저장")
    @Test
    void create() {
        /**
         *  테스트 db 데이터 생성하고 만들겠습니다.
         */
    }

    @Test
    void testJpa() {
        Member member = memberService.findByEmail("asd@asd");
        Article article = articleRepository.findById(1).get();
        for (int i = 1; i <= 60; i++) {
            String content = "내용무";
            this.commentService.create(article, content, member);
        }
    }
}