package likelion.ylw.initDB;

import likelion.ylw.article.*;
import likelion.ylw.category.Category;
import likelion.ylw.category.CategoryRepository;
import likelion.ylw.category.CategoryService;
import likelion.ylw.comment.CommentService;
import likelion.ylw.member.Member;
import likelion.ylw.member.MemberService;
import likelion.ylw.notice.NoticeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class InitDB {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private MemberService memberService;

    @Autowired
    private ArticleItemService articleItemService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private NoticeService noticeService;


    @Test
    @DisplayName("유저 3명 생성")
    void t1() {
        Member member1 = memberService.create("user1","12345","user1@naver.com", "김유저");
        Member member2 = memberService.create("user2","12345","user2@naver.com", "이유저");
        Member member3 = memberService.create("user3","12345","user3@naver.com", "박유저");
        Member member4 = memberService.create("user4","12345","user4@naver.com", "나유저");

    }
    @Test
    @DisplayName("카테고리 3개 생성")
    void t2() {
        Category category1 = new Category();
        category1.setName("스포츠");
        categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setName("정치");
        categoryRepository.save(category2);

        Category category3 = new Category();
        category3.setName("문화");
        categoryRepository.save(category3);
    }

    @Test
    @DisplayName("각 카테고리 별 게시글 100개씩 생성")
    void t3() {
        Member member1 = memberService.getMemberId("user1");
        Member member2 = memberService.getMemberId("user2");
        Article article1;


        for (int i = 1; i <= 3; i++) {
            Category category1 = categoryService.findById(i);
            for (int j = 1; j <= 100; j++) {
                if(j%2==0) {
                    article1 = articleService.create(
                            category1.getName()+"의" +j+"번 게시물 제목입니다",
                            "내용",
                            "user1",
                            i
                    );
                } else {
                    article1 = articleService.create(
                            category1.getName()+"의" +j+"번 게시물 제목입니다",
                            "내용",
                            "user2",
                            i
                    );
                }

                ArticleItem articleItem1 = articleItemService.create(article1, "맞다");
                ArticleItem articleItem2 = articleItemService.create(article1, "틀리다");
            }
        }
    }

    @Test
    @DisplayName("1번 카테고리의 1번 게시글에 댓글 100개 추가")
    void t4() {
        Member member1 = memberService.getMemberId("user1");
        Member member2 = memberService.getMemberId("user2");
        Article article = articleRepository.findById(1).get();
        for (int i = 1; i <= 200; i++) {
            if (i%2==0) {
                String content = "본인 게시글 본인 댓글 내용입니다.";
                this.commentService.create(article, content, member1);
            } else {
                String content = "다른 사람 댓글 내용입니다";
                this.commentService.create(article, content, member2);
            }
        }
    }

    @Test
    @DisplayName("공지사항 100개 추가")
    void t5() {
        Member member = memberService.getMemberId("user1");
        for (int i = 0; i < 100; i++) {
            noticeService.create(i+"번째 공지입니다", "내용입니다",member);
        }

    }

}
