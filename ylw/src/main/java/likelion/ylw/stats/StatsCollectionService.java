package likelion.ylw.stats;

import likelion.ylw.article.ArticleItemRepository;
import likelion.ylw.member.Member;
import likelion.ylw.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatsCollectionService {
    private final StatsCollectionRepository statsCollectionRepository;
    private final ArticleItemRepository articleItemRepository;
    private final MemberRepository memberRepository;

    public void createStatsCollection(Integer articleItemId, Integer age, String gender, String userName) {
        StatsCollection statsCollection = new StatsCollection();
        statsCollection.setArticleItem(articleItemRepository.findById(articleItemId).get());
        statsCollection.setAge(age);
        statsCollection.setGender(gender);
        if (userName.equals("anonymous")) {
            statsCollection.setMember(null);
        } else {
            statsCollection.setMember(memberRepository.findByMemberId(userName).get());
        }

        statsCollectionRepository.save(statsCollection);
    }
    public List<StatsCollection> findByMember(Member isVoter) {
        List<StatsCollection> statsCollectionList = statsCollectionRepository.findByMember(isVoter);

        return statsCollectionList;
    }
}
