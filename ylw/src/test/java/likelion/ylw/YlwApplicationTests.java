package likelion.ylw;

import likelion.ylw.article.Article;
import likelion.ylw.article.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class YlwApplicationTests {

    @Autowired
    private ArticleRepository articleRepository;


    @Test
    void contextLoads() {
    }

    @Test
    void articleTest() {
        Article article = new Article();
        article.setTitle("제목1");
        article.setContent("1번글 입니다");
        article.setBlind(false);

        articleRepository.save(article);

        Article article2 = new Article();
        article2.setTitle("제목2");
        article2.setContent("2번글 입니다");
        article2.setBlind(false);

        articleRepository.save(article2);
    }

    @Test
    @Transactional
    void findByTitle테스트() {
        Article article = articleRepository.findByTitle("제목1").get();
        article.getCreatedDate();

        System.out.println(article.getCreatedDate());
    }
}
