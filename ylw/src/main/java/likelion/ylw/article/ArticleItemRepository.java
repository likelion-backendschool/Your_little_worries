package likelion.ylw.article;

import likelion.ylw.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleItemRepository extends JpaRepository<ArticleItem, Integer> {
}
