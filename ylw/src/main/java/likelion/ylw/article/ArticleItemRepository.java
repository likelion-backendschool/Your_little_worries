package likelion.ylw.article;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleItemRepository extends JpaRepository<ArticleItem, Integer> {
    List<ArticleItem> findArticleItemByArticleId(Integer articleId);

    ArticleItem findMaxArticleItemByArticleId(int maxId);
}
