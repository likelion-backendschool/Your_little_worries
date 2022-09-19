package likelion.ylw.article.recommend;

import likelion.ylw.article.Article;
import likelion.ylw.comment.Comment;
import likelion.ylw.comment.vote.CommentVote;
import likelion.ylw.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class ArticleRecommendService {

    private final ArticleRecommendRepository articleRecommendRepository;

    /**
     * 좋아요 추가
     */
    public void pushVoteBtn(Article article, Member member) {

        boolean result = articleRecommendRepository.existsByMemberAndArticle(member, article);
        if (result) {
            articleRecommendRepository.deleteByMemberAndArticle(member, article);
            // 이미 좋아요를 누른 상태인 경우 취소해줌
        } else {
            // 좋아요가 아닌 상태인 경우 추가해줌
            ArticleRecommend articleRecommend = new ArticleRecommend();
            articleRecommend.setMember(member);
            articleRecommend.setArticle(article);
            articleRecommendRepository.save(articleRecommend);
        }
    }

    public Set<Article> getArticlesByMember(Member member) {
        return articleRecommendRepository.findByMember(member);
    }

    public boolean existsByMemberAndArticle(Member member, Article article) {
        boolean result = articleRecommendRepository.existsByMemberAndArticle(member, article);

        return result;
    }
}
