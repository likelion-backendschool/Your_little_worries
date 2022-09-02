package likelion.ylw.comment;

import likelion.ylw.article.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Page<Comment> findByArticle(Article article, Pageable pageable);
    Integer countByTempNicknameAndTempPassword(String tempNickname, String tempPassword);
}
