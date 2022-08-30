package likelion.ylw.comment;

import likelion.ylw.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByArticle(Article article);

    Integer countByTempNicknameAndTempPassword(String tempNickname, String tempPassword);
}
