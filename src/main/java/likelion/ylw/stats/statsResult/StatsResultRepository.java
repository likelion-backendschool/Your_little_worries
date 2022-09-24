package likelion.ylw.stats.statsResult;

import likelion.ylw.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StatsResultRepository extends JpaRepository<StatsResult, Integer> {
    Optional<StatsResult> findByArticle(Article article);
}
