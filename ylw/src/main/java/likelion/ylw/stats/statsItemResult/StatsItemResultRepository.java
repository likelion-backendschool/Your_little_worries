package likelion.ylw.stats.statsItemResult;

import likelion.ylw.article.Article;
import likelion.ylw.article.ArticleItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StatsItemResultRepository extends JpaRepository<StatsItemResult, Integer> {
    Optional<StatsItemResult> findByArticleItem(ArticleItem articleItem);

    List<StatsItemResult> findAllByArticle(Article article);
}
