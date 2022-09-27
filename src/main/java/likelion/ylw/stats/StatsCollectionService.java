package likelion.ylw.stats;

import likelion.ylw.article.Article;
import likelion.ylw.article.ArticleItem;
import likelion.ylw.article.ArticleItemRepository;
import likelion.ylw.member.Member;
import likelion.ylw.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatsCollectionService {
    private final StatsCollectionRepository statsCollectionRepository;
    private final ArticleItemRepository articleItemRepository;
    private final MemberRepository memberRepository;

    public void createStatsCollection(Integer articleItemId, Integer age, String gender, String userName, String IP,
                                      Article article) {
        StatsCollection statsCollection = new StatsCollection();
        statsCollection.setArticleItem(articleItemRepository.findById(articleItemId).get());
        statsCollection.setAge(age);
        statsCollection.setGender(gender);
        statsCollection.setArticle(article);
        if (userName.equals("anonymous")) {
            statsCollection.setMember(null);
        } else {
            statsCollection.setMember(memberRepository.findByMemberId(userName).get());
        }
        statsCollection.setIP(IP);

        statsCollectionRepository.save(statsCollection);
    }
    public List<StatsCollection> findByMember(Member isVoter) {
        List<StatsCollection> statsCollectionList = statsCollectionRepository.findByMember(isVoter);

        return statsCollectionList;
    }

    public List<StatsCollection> findByIP(String IP) {
        List<StatsCollection> statsCollectionList = statsCollectionRepository.findByIP(IP);

        return statsCollectionList;
    }

    public List<StatsCollection> findByArticle(Article article) {
        List<StatsCollection> statsCollectionList =statsCollectionRepository.findByArticle(article);

        return statsCollectionList;
    }
}
