package likelion.ylw.initDB;

import likelion.ylw.article.*;
import likelion.ylw.category.Category;
import likelion.ylw.category.CategoryRepository;
import likelion.ylw.category.CategoryService;
import likelion.ylw.comment.CommentService;
import likelion.ylw.member.Member;
import likelion.ylw.member.MemberService;
import likelion.ylw.notice.NoticeService;
import likelion.ylw.stats.statsResult.StatsResultService;
import org.junit.jupiter.api.BeforeAll;
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
    @Autowired
    private StatsResultService statsResultService;

    @Test
    @DisplayName("유저 20명 생성")
    void t1() {
        Member member1 = memberService.create("member1","1234","member1@test.com", "김멤버");
        Member member2 = memberService.create("member2","1234","member2@test.com", "이멤버");
        Member member3 = memberService.create("member3","1234","member3@test.com", "박멤버");
        Member member4 = memberService.create("member4","1234","member4@test.com", "나멤버");
        Member member5 = memberService.create("member5","1234","member5@test.com", "최멤버");
        Member member6 = memberService.create("member6","1234","member6@test.com", "하멤버");
        Member member7 = memberService.create("member7","1234","member7@test.com", "기멤버");
        Member member8 = memberService.create("member8","1234","member8@test.com", "나멤버");
        Member member9 = memberService.create("member9","1234","member9@test.com", "다멤버");
        Member member10 = memberService.create("member10","1234","member10@test.com", "라멤버");
        Member member11 = memberService.create("member11","1234","member11@test.com", "사멤버");
        Member member12 = memberService.create("member12","1234","member12@test.com", "아멤버");
        Member member13 = memberService.create("member13","1234","member13@test.com", "자멤버");
        Member member14 = memberService.create("member14","1234","member14@test.com", "차멤버");
        Member member15 = memberService.create("member15","1234","member15@test.com", "카멤버");
        Member member16 = memberService.create("member16","1234","member16@test.com", "타멤버");
        Member member17 = memberService.create("member17","1234","member17@test.com", "파멤버");
        Member member18 = memberService.create("member18","1234","member18@test.com", "고멤버");
        Member member19 = memberService.create("member19","1234","member19@test.com", "바멤버");
        Member member20 = memberService.create("member20","1234","member20@test.com", "우멤버");
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

                statsResultService.create(article1);
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

    @Test
    @DisplayName("인기 게시글 6개 생성")
    void t6() {
        Member member1 = memberService.findByMemberId("member1");
        Member member2 = memberService.findByMemberId("member2");
        Member member3 = memberService.findByMemberId("member3");
        Member member4 = memberService.findByMemberId("member4");
        Member member5 = memberService.findByMemberId("member5");
        Member member6 = memberService.findByMemberId("member6");

        Article article1 = articleService.createPupular("카공할 때", "중간에 나가서 점심먹고와도 될까???", member1.getMemberId(), 1, 100L);
        articleItemService.create(article1, "이미 페이했으니 괜찮다");
        articleItemService.create(article1, "민폐여서 안된다");

        Article article2 = articleService.createPupular("소개팅할 때", "파스타 돌려먹어야할까요", member2.getMemberId(), 2, 100L);
        articleItemService.create(article2, "맞다");
        articleItemService.create(article2, "틀리다");
        Article article3 = articleService.createPupular("파인애플 피자", "맛있니??", member3.getMemberId(), 3, 100L);
        articleItemService.create(article3, "맛있다");
        articleItemService.create(article3, "먹을 수 없다");
        Article article4 = articleService.createPupular("친구가 말을할 때", "입냄새가 나는데 말을 해줘야할까 말아야할까??", member4.getMemberId(), 4, 100L);
        articleItemService.create(article4, "해줘야한다");
        articleItemService.create(article4, "말아야한다");
        Article article5 = articleService.createPupular("직장인 고민거리", "오늘 뭐먹지??", member5.getMemberId(), 5, 100L);
        articleItemService.create(article5, "김치볶음밥");
        articleItemService.create(article5, "짜장면");
        articleItemService.create(article5, "돼지고기 김치찌개");
        articleItemService.create(article5,"콩나물해장국");
        Article article6 = articleService.createPupular("내 지갑에 50만원이 있으면", "50만원으로 명품 옷을 살까 아니면 그냥 적당한 브랜드 옷을 여러개 살까 아니면 비브랜드 저렴한 옷을 왕창 살까", member6.getMemberId(), 6, 100L);
        articleItemService.create(article6, "명품옷");
        articleItemService.create(article6, "적당한 브랜드옷");
        articleItemService.create(article6, "저렴한옷 많이");

        statsResultService.create(article1);
        statsResultService.create(article2);
        statsResultService.create(article3);
        statsResultService.create(article4);
        statsResultService.create(article5);
        statsResultService.create(article6);
    }

}
