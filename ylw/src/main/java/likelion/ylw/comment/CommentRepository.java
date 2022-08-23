package likelion.ylw.comment;

import likelion.ylw.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
