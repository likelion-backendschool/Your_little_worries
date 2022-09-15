package likelion.ylw.stats.statsResult;

import likelion.ylw.article.Article;
import likelion.ylw.article.ArticleRepository;
import likelion.ylw.stats.statsItemResult.StatsItemResult;
import likelion.ylw.stats.statsItemResult.StatsItemResultRepository;
import org.apache.commons.math3.stat.inference.ChiSquareTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
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
        Article article = articleRepository.findById(2).get();

        List<StatsItemResult> statsItemResults = statsItemResultRepository.findAllByArticle(article); // 항목별 투표 결과
        StatsResult statsResult = statsResultRepository.findByArticle(article).get();

        int row = statsItemResults.size(); // 항목 개수
        int column = 8;                    // 구해야 될 결과값 개수

        long[][] counts = new long[row][column];

        // 카이제곱에 메서드에 넣기위해 이중배열에 데이터를 넣어주는 과정
        int index = 0;
        for (StatsItemResult statsItemResult : statsItemResults) {
            long[] count = { statsItemResult.getTotal(), statsItemResult.getMaleTotal(), statsItemResult.getFemaleTotal(),
                    statsItemResult.getTotal10(), statsItemResult.getTotal20(), statsItemResult.getTotal30(),statsItemResult.getTotal40(), statsItemResult.getTotalOver50()
            };
            counts[index++] = count;
        }

        // 계산하기 편하도록 2차배열을 90도 회전해줌
        counts = transpose(counts);

        // 카이제곱검증 코드
        for (int i = 0; i < column; i++) {
            long[] observed = counts[i];
            double[] expected = new double[row];

            // 기대값 구해줌
            long sum = Arrays.stream(observed).sum();
            double avg = sum/row;
            Arrays.fill(expected, avg);


            ChiSquareTest t = new ChiSquareTest();
            boolean result = t.chiSquareTest(expected, observed,0.05); // 카이제곱 결과
            double result2 = t.chiSquareTest(expected, observed); // 카이제곱 결과
            double result3 = t.chiSquare(expected, observed); // 카이제곱 결과
            System.out.println(i+" result2 = " + result2 +"result3"+result3);

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
        /**
         * 비교 대상 둘 다 0일 경우 에러
         */
    }
    @Test
    void transposeTest() {
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
        long[][] b = transpose(a);

        for (long[] longs : b) {
            for (long aLong : longs) {
                System.out.print(aLong+" ");
            }
            System.out.println(" ");
        }
    }
    public long[][] transpose(long[][] counts) {
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