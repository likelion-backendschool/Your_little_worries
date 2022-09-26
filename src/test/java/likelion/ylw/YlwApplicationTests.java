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

    @Test
    void contextLoads() {
    }
}
