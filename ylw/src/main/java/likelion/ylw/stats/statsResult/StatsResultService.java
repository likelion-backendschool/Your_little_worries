package likelion.ylw.stats.statsResult;

import likelion.ylw.article.Article;
import likelion.ylw.article.ArticleItem;
import likelion.ylw.article.ArticleItemRepository;
import likelion.ylw.util.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.stat.inference.ChiSquareTest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatsResultService {

    private final StatsResultRepository statsResultRepository;
    private final ArticleItemRepository articleItemRepository;

    private static int COLUMN  = 8; // 결과값 개수

    /**
     * 투표지 생성 시 투표결과 db도 같이 만들어줌
     */
    public void create(Article article) {
        StatsResult statsResult = new StatsResult();
        statsResult.setArticle(article);
        statsResultRepository.save(statsResult);
    }
    /**
     * 해당 게시글의 투표결과 반환
     */
    public StatsResult getStatsResultByArticle(Article article) {
        Optional<StatsResult> statsResult = statsResultRepository.findByArticle(article);
        if (statsResult.isPresent()) {
            return statsResult.get();
        }
        throw new DataNotFoundException("결과를 찾지 못했습니다.");
    }
    /**
     * 카이제곱 계산
     * 총 투표수, 남자, 여자, 각각 나이대 별
     * 항목의 투표수가 모두 0인 경우 오류나는 상태
     */
    public void calculate(Article article) {
        List<ArticleItem> articleItems = articleItemRepository.findArticleItemByArticleId(article.getId()); // 항목별 투표 결과
        StatsResult statsResult = statsResultRepository.findByArticle(article).get();


        // 카이제곱에 메서드에 넣기위해 이중배열에 데이터를 넣어주는 과정
        long[][] counts = listTo2DArray(articleItems);
        int row = articleItems.size(); // 항목 개수

        // 계산하기 편하도록 2차배열을 90도 회전해줌
        counts = transpose(counts);

        // 카이제곱검증 코드
        for (int i = 0; i < COLUMN; i++) {
            long[] observed = counts[i];
            double[] expected = new double[row];

            // 투표를 하나도 안 한 투표지인 경우 계산하면 안 됨
            if (Arrays.stream(observed).filter(item -> item == 0).count() == counts[0].length) {
                continue;
            }
            // 기대값 구해줌
            long sum = Arrays.stream(observed).sum();
            double avg = (double) sum/row; // 항목별 총투표수 / 항목개수
            Arrays.fill(expected, avg);

            ChiSquareTest t = new ChiSquareTest();
            boolean result = t.chiSquareTest(expected, observed,0.05); // 카이제곱 결과

            // 결과 db 적용
            switch (i) {
                case 0 -> statsResult.setResult(result);
                case 1 -> statsResult.setMale(result);
                case 2 -> statsResult.setFemale(result);
                case 3 -> statsResult.setAge10(result);
                case 4 -> statsResult.setAge20(result);
                case 5 -> statsResult.setAge30(result);
                case 6 -> statsResult.setAge40(result);
                case 7 -> statsResult.setAgeOver50(result);
            }

        }
        statsResultRepository.save(statsResult);
    }

    /**
     * StatsItemResult리스트를 이중배열로 변환해줌
     */
    public long[][] listTo2DArray(List<ArticleItem> ArticleItems) {
        long[][] counts = new long[ArticleItems.size()][COLUMN]; // [항목수][결과수]
        int index = 0;
        for (ArticleItem articleItem : ArticleItems) {
            long[] count = { articleItem.getTotal(), articleItem.getMaleTotal(), articleItem.getFemaleTotal(),
                    articleItem.getTotal10(), articleItem.getTotal20(), articleItem.getTotal30(),articleItem.getTotal40(), articleItem.getTotalOver50()
            };
            counts[index++] = count;
        }
        return counts;
    }

    /**
     * 2차월 배열 transpose
     */
    public long[][] transpose(long[][] counts) {
        long[][] newCounts = new long[counts[0].length][counts.length];

        for (int i = 0; i < counts.length; i++) {
            for (int j = 0; j < counts[0].length; j++) {
                newCounts[j][i] = counts[i][j];
            }
        }
        return newCounts;
    }
}
