package likelion.ylw;

import likelion.ylw.article.Article;
import likelion.ylw.article.ArticleRepository;
import likelion.ylw.category.Category;
import likelion.ylw.category.CategoryRepository;
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

    @Autowired
    private CategoryRepository categoryRepository;


    @Test
    void contextLoads() {
    }

    @Test
    void articleTest() {
        Category category1 = new Category();
        category1.setName("스포츠");
        categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setName("정치");
        categoryRepository.save(category2);

        Category category3 = new Category();
        category3.setName("문화");
        categoryRepository.save(category3);

//        Article article = new Article();
//        article.setTitle("제목1");
//        article.setContent("1번글 입니다");
//        article.setBlind(false);
//        article.setCategory(category1);
//
//        articleRepository.save(article);
//
//        Article article2 = new Article();
//        article2.setTitle("제목2");
//        article2.setContent("2번글 입니다");
//        article2.setBlind(false);
//        article2.setCategory(category2);
//
//        articleRepository.save(article2);
//
//        Article article3 = new Article();
//        article3.setTitle("제목1");
//        article3.setContent("3번글 입니다");
//        article3.setBlind(false);
//        article3.setCategory(category3);
//
//        articleRepository.save(article3);
    }

    @Test
    @Transactional
    void findByTitle테스트() {
        Article article = articleRepository.findByTitle("제목1").get();
        article.getCreatedDate();

        System.out.println(article.getCreatedDate());
    }
}
