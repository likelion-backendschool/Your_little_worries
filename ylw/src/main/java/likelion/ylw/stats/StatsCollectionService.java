package likelion.ylw.stats;

import likelion.ylw.article.ArticleItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsCollectionService {
    private final StatsCollectionRepository statsCollectionRepository;
    private final ArticleItemRepository articleItemRepository;

    public void createStatsCollection(Integer articleItemId, Integer age, String gender) {
        StatsCollection statsCollection = new StatsCollection();
        statsCollection.setArticleItem(articleItemRepository.findById(articleItemId).get());
        statsCollection.setAge(age);
        statsCollection.setGender(gender);

        statsCollectionRepository.save(statsCollection);

    }
}
