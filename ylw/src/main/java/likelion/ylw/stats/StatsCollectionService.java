package likelion.ylw.stats;

import likelion.ylw.article.ArticleItemRepository;
import likelion.ylw.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        statsCollection.setMember(memberRepository.findByMemberId(userName).get());

        statsCollectionRepository.save(statsCollection);

    }
}
