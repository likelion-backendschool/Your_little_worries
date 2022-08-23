package likelion.ylw.comment;

import likelion.ylw.article.Article;
import likelion.ylw.article.ArticleRepository;
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

    @Transactional
    @DisplayName("댓글을 DB에 저장")
    @Test
    void create() {
        /**
         *  테스트 db 데이터 생성하고 만들겠습니다.
         */
    }
}