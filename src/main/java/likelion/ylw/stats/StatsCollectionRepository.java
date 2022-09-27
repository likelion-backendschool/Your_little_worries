package likelion.ylw.stats;

import likelion.ylw.article.Article;
import likelion.ylw.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StatsCollectionRepository extends JpaRepository<StatsCollection, Integer> {
    List<StatsCollection> findByMember(Member member);

    List<StatsCollection> findByIP(String Ip);
    List<StatsCollection> findByArticle(Article article);
}
