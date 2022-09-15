package likelion.ylw.article.recommend;

import likelion.ylw.article.Article;
import likelion.ylw.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface ArticleRecommendRepository extends JpaRepository<ArticleRecommend, Long> {

    boolean existsByMemberAndArticle(Member member, Article article);

    @Transactional
        // 삭제의 경우
    void deleteByMemberAndArticle(Member member, Article article);

    @Query("select ar.article from ArticleRecommend ar where ar.member=:member")
    Set<Article> findByMember(@Param("member") Member member);
}
