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
    @DisplayName("유저 6명 생성")
    void t1() {
        Member member1 = memberService.create("member1","1234","member1@test.com", "김멤버");
        Member member2 = memberService.create("member2","1234","member2@test.com", "이멤버");
        Member member3 = memberService.create("member3","1234","member3@test.com", "박멤버");
        Member member4 = memberService.create("member4","1234","member4@test.com", "나멤버");
        Member member5 = memberService.create("member5","1234","member5@test.com", "최멤버");
        Member member6 = memberService.create("member6","1234","member6@test.com", "하멤버");
    }

    @Test
    @DisplayName("카테고리 6개 생성")
    void t2() {
        Category category1 = new Category();
        category1.setName("일상");
        categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setName("데이트");
        categoryRepository.save(category2);

        Category category3 = new Category();
        category3.setName("음식");
        categoryRepository.save(category3);

        Category category4 = new Category();
        category4.setName("관계");
        categoryRepository.save(category4);

        Category category5 = new Category();
        category5.setName("직장");
        categoryRepository.save(category5);

        Category category6 = new Category();
        category6.setName("패션");
        categoryRepository.save(category6);
    }

    @Test
    @DisplayName("각 카테고리 별 게시글 100개씩 생성")
    void t3() {
        Member member1 = memberService.findByMemberId("member1");
        Member member2 = memberService.findByMemberId("member2");
        Article article1;


        for (int i = 1; i <= 6; i++) {
            Category category1 = categoryService.findById(i);
            for (int j = 1; j <= 100; j++) {
                if(j%2==0) {
                    article1 = articleService.create(
                            category1.getName()+"의" +j+"번 게시물 제목입니다",
                            "내용",
                            "member1",
                            i
                    );
                } else {
                    article1 = articleService.create(
                            category1.getName()+"의" +j+"번 게시물 제목입니다",
                            "내용",
                            "member2",
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
        Member member1 = memberService.findByMemberId("member1");
        Member member2 = memberService.findByMemberId("member2");
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
        Member member = memberService.findByMemberId("member1");
        for (int i = 0; i < 100; i++) {
            noticeService.create(i+"번째 공지입니다", "내용입니다",member);
        }

    }

}
