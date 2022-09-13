package likelion.ylw.stats.statsResult;

import likelion.ylw.article.Article;
import likelion.ylw.stats.statsItemResult.StatsItemResult;
import likelion.ylw.stats.statsItemResult.StatsItemResultRepository;
import likelion.ylw.util.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.stat.inference.ChiSquareTest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatsResultService {

    private final StatsResultRepository statsResultRepository;
    private final StatsItemResultRepository statsItemResultRepository;

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
    public StatsResult getStatsResult(Article article) {
        Optional<StatsResult> statsResult = statsResultRepository.findByArticle(article);
        if (statsResult.isPresent()) {
            return statsResult.get();
        }
        throw new DataNotFoundException("결과를 찾지 못했습니다.");
    }
    /**
     * 나이별 카이제곱 계산
     * 비교 대상 둘 다 0일 경우 NaN를 반환함
     */
    public void calculate(Article article) {
        List<StatsItemResult> statsItemResults = statsItemResultRepository.findAllByArticle(article);
        StatsResult statsResult = statsResultRepository.findByArticle(article).get();

        int row = statsItemResults.size();
        int column = 4;

        ChiSquareTest t = new ChiSquareTest();
        long[][] counts = new long[row][column];

        // 카이제곱에 메서드에 넣기위해 이중배열에 데이터를 넣어주는 과정
        int index = 0;
        for (StatsItemResult statsItemResult : statsItemResults) {
            long[] count = {statsItemResult.getTotal10(), statsItemResult.getTotal20(), statsItemResult.getTotal30(), statsItemResult.getTotalOver40()};
            counts[index++] = count;
        }

        // 10대-20대, 10대-30대, ... 30대-40대 각각 비교하여 카이제곱검증 결과를 db에 넣어줌
        index = 0;
        long[][] turnCounts = transpose(counts);
        for (int i = 0; i < column-1; i++) {
            // 비교대상 A
            long[] countA = turnCounts[i];
            for (int j = i+1; j < column; j++) {
                // 비교대상 B
                long[] countB = turnCounts[j];
                // 카이제곱 계산
                long[][] countAB = {countA, countB};
                double v1 = t.chiSquareTest(countAB);                  // 산출된 검정 통계량
                double v2 = t.chiSquare(countAB);                      // p-value
                boolean result = t.chiSquareTest(countAB,0.05);  // 유의미 결과

                // 결과 db 적용
                switch (index) {
                    case 0 -> statsResult.setCompare10And20(result);
                    case 1 -> statsResult.setCompare10And30(result);
                    case 2 -> statsResult.setCompare10And40(result);
                    case 3 -> statsResult.setCompare20And30(result);
                    case 4 -> statsResult.setCompare20And40(result);
                    default -> statsResult.setCompare30And40(result);
                }
                index++;
            }
        }
        statsResultRepository.save(statsResult);
    }

    /**
     * 2차월 배열 transpose
     */
    private long[][] transpose(long[][] counts) {
        long[][] newCounts = new long[counts[0].length][counts.length];

        for (int i = 0; i < counts.length; i++) {
            for (int j = 0; j < counts[0].length; j++) {
                newCounts[j][i] = counts[i][j];
            }
        }
        return newCounts;
    }
}
