package likelion.ylw.stats.statsItemResult;

import likelion.ylw.article.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class StatsItemResultService {

    private final StatsItemResultRepository statsItemResultRepository;
    private final ArticleItemRepository articleItemRepository;

    /**
     * 해당 투표지의 항목별 결과 가져오기
     */
    public List<StatsItemResult> getStatsItemResultsByArtcle(Article article) {
        return statsItemResultRepository.findAllByArticle(article);
    }
    /**
     *  투표지 생성 시 항목별 투표 합계 db도 같이 만들어줌
     */
    public void create(Article article, ArticleItem articleItem) {
        StatsItemResult statsItemResult = new StatsItemResult();

        statsItemResult.setArticle(article);
        statsItemResult.setArticleItem(articleItem);

        statsItemResultRepository.save(statsItemResult);
    }
    /**
     *  투표를 합계에 적용
     */
    public void plusResult(Integer articleItemId, Integer age, String gender) {

        ArticleItem articleItem = articleItemRepository.findById(articleItemId).get();
        StatsItemResult statsItemResult = statsItemResultRepository.findByArticleItem(articleItem).get();

        statsItemResult.setTotal(statsItemResult.getTotal()+1);

        if (gender.equals("male")) {
            statsItemResult.setMaleTotal(statsItemResult.getMaleTotal()+1);
        } else {
            statsItemResult.setFemaleTotal(statsItemResult.getFemaleTotal()+1);
        }

        switch (age) {
            case 10 -> statsItemResult.setTotal10(statsItemResult.getTotal10()+1);
            case 20 -> statsItemResult.setTotal20(statsItemResult.getTotal20()+1);
            case 30 -> statsItemResult.setTotal30(statsItemResult.getTotal30()+1);
            case 40 -> statsItemResult.setTotal40(statsItemResult.getTotal40()+1);
            default -> statsItemResult.setTotalOver50(statsItemResult.getTotalOver50()+1);
        }
        statsItemResultRepository.save(statsItemResult);
    }
}
