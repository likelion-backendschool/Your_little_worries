package likelion.ylw.stats.statsResult;

import likelion.ylw.article.Article;
import likelion.ylw.article.ArticleRepository;
import likelion.ylw.stats.statsItemResult.StatsItemResult;
import likelion.ylw.stats.statsItemResult.StatsItemResultRepository;
import org.apache.commons.math3.stat.inference.ChiSquareTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StatsResultServiceTest {

    @Autowired
    StatsItemResultRepository statsItemResultRepository;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    StatsResultRepository statsResultRepository;
    @Test
    void create() {
    }

    @Test
    void calculate() {
        Article article = articleRepository.findById(4).get();

        List<StatsItemResult> statsItemResults = statsItemResultRepository.findAllByArticle(article);
        StatsResult statsResult = statsResultRepository.findByArticle(article).get();

        int row = statsItemResults.size();
        int column = 4;

        ChiSquareTest t = new ChiSquareTest();
        long[][] counts = new long[row][column];

        int index = 0;
        // 카이제곱에 메서드에 넣기위해 이중배열에 데이터를 넣어주는 과정
        for (StatsItemResult statsItemResult : statsItemResults) {
            long[] count = {statsItemResult.getTotal10(), statsItemResult.getTotal20(), statsItemResult.getTotal30(), statsItemResult.getTotalOver40()};
            counts[index++] = count;
        }
        long[][] turnCounts = turn(counts);
        index = 0;
        for (int i = 0; i < column-1; i++) {
            // 비교대상 A
            long[] countA = turnCounts[i];
            for (int j = i+1; j < column; j++) {
                // 비교대상 B
                long[] countB = turnCounts[j];
                long[][] countAB = {countA, countB};

                double v1 = t.chiSquareTest(countAB);
                double v2 = t.chiSquare(countAB);
                boolean result = t.chiSquareTest(countAB,0.05);
                System.out.println(i+1+"와"+(j+1)+"의 p-value = " + v1);
                System.out.println(i+1+"와"+(j+1)+"의 카이제곱통게값 = " + v2);
                System.out.println(i+1+"와"+(j+1)+"의 결과 = " + result);
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
        /**
         * 비교 대상 둘 다 0일 경우 NaN를 반환함
         */
    }
    @Test
    void trunTest() {
        long[][] a = {
                {1,2,3,4},
                {5,6,7,8},
                {9,10,11,12}
        };
        System.out.println("a = " + a);
        for (long[] longs : a) {
            for (long aLong : longs) {
                System.out.print(aLong+" ");
            }
            System.out.println(" ");
        }
        long[][] b = turn(a);

        for (long[] longs : b) {
            for (long aLong : longs) {
                System.out.print(aLong+" ");
            }
            System.out.println(" ");
        }
    }
    public long[][] turn(long[][] counts) {
        // 2,4   4,2
        long[][] newCounts = new long[counts[0].length][counts.length];

        for (int i = 0; i < counts.length; i++) {
            for (int j = 0; j < counts[0].length; j++) {
                newCounts[j][i] = counts[i][j];
            }
        }
        return newCounts;
    }
}