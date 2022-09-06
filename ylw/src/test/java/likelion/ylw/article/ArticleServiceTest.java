package likelion.ylw.article;

import likelion.ylw.member.Member;
import likelion.ylw.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleItemService articleItemService;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("투표지 100개 생성")
    public void createArticle100() {
        for (int i = 0; i < 100; i++){
            Article article = articleService.create(
                    String.format("스포츠제목%d", i), String.format("스포츠설명%d", i), "member2",1
            );
            articleItemService.create(article, "네");
            articleItemService.create(article, "아니요");
        }

        for (int i = 0; i < 100; i++){
            Article article = articleService.create(
                    String.format("정치제목%d", i), String.format("정치설명%d", i), "member2",2
            );
            articleItemService.create(article, "네");
            articleItemService.create(article, "아니요");
            articleItemService.create(article, "증립");
        }

        for (int i = 0; i < 100; i++){
            Article article = articleService.create(
                    String.format("문화제목%d", i), String.format("문화설명%d", i), "member2",3
            );
            articleItemService.create(article, "1");
            articleItemService.create(article, "2");
            articleItemService.create(article, "3");
            articleItemService.create(article, "4");
        }
    }
}
