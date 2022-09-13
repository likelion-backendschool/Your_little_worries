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
public class TestDB {
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
    @DisplayName("각 카테고리 별 게시글 1~2개씩 생성")
    void t3() {
        Member member1 = memberService.findByMemberId("member1");
        Member member2 = memberService.findByMemberId("member2");
        Member member3 = memberService.findByMemberId("member3");
        Member member4 = memberService.findByMemberId("member4");
        Member member5 = memberService.findByMemberId("member5");
        Member member6 = memberService.findByMemberId("member6");

        Article article1 = articleService.create("카공할 때", "중간에 나가서 점심먹고와도 될까???", member1.getMemberId(), 1);
        articleItemService.create(article1, "이미 페이했으니 괜찮다");
        articleItemService.create(article1, "민폐여서 안된다");
        Article article2 = articleService.create("소개팅할 때", "파스타 돌려먹어야할까요", member2.getMemberId(), 2);
        articleItemService.create(article2, "맞다");
        articleItemService.create(article2, "틀리다");
        Article article3 = articleService.create("파인애플 피자", "맛있니??", member3.getMemberId(), 3);
        articleItemService.create(article3, "맛있다");
        articleItemService.create(article3, "먹을 수 없다");
        Article article4 = articleService.create("친구가 말을할 때", "입냄새가 나는데 말을 해줘야할까 말아야할까??", member4.getMemberId(), 4);
        articleItemService.create(article4, "해줘야한다");
        articleItemService.create(article4, "말아야한다");
        Article article5 = articleService.create("직장인 고민거리", "오늘 뭐먹지??", member5.getMemberId(), 5);
        articleItemService.create(article5, "김치볶음밥");
        articleItemService.create(article5, "짜장면");
        articleItemService.create(article5, "돼지고기 김치찌개");
        articleItemService.create(article5,"콩나물해장국");
        Article article6 = articleService.create("내 지갑에 50만원이 있으면", "50만원으로 명품 옷을 살까 아니면 그냥 적당한 브랜드 옷을 여러개 살까 아니면 비브랜드 저렴한 옷을 왕창 살까", member6.getMemberId(), 6);
        articleItemService.create(article6, "명품옷");
        articleItemService.create(article6, "적당한 브랜드옷");
        articleItemService.create(article6, "저렴한옷 많이");
    }
}
