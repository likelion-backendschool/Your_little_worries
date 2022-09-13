package likelion.ylw.comment;

import javassist.bytecode.annotation.MemberValue;
import likelion.ylw.article.Article;
import likelion.ylw.article.ArticleRepository;
import likelion.ylw.member.Member;
import likelion.ylw.member.MemberService;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.stat.inference.ChiSquareTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CommentServiceTest {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private CommentService commentService;


    @Transactional
    @DisplayName("댓글을 DB에 저장")
    @Test
    void create() {
        /**
         *  테스트 db 데이터 생성하고 만들겠습니다.
         */
    }

    @Test
    void testJpa() {
        Member member = memberService.findByEmail("asd@asd");
        Article article = articleRepository.findById(1).get();
        for (int i = 1; i <= 60; i++) {
            String content = "내용무";
            this.commentService.create(article, content, member);
        }
    }
    @Test
    @DisplayName("카이제곱검증 라이브러리 테스트")
    void test() {
        double[] expected = new double[]
                {
                        20 , 20, 20, 20, 20
                };

        // Example 1: Reject null hypothesis, observed doesnt fit

        long[] observed1 = new long[]
                {
                        18,  16,  15,  29,  22,
                };

        chiTest(expected, observed1);


    }
    public static void chiTest(double[] expected, long[] observed)
    {
        double alpha = 0.05; // confidence level 99%

        System.out.println();

        System.out.printf("%15.15s: ", "Observed");
        for (int i = 0; i < observed.length; i++) {
            System.out.printf("%-6d ", observed[i]);
        }

        System.out.println();

        System.out.printf("%15.15s: ", "Expected");
        for (int i = 0; i < expected.length; i++) {
            System.out.printf("%-5.1f ", expected[i]);
        }

        ChiSquareTest t = new ChiSquareTest();

        double pval1 = t.chiSquareTest(expected, observed); // p-value
        double pval2 = t.chiSquare(expected, observed); // 검정통계량 값
        boolean pval3 = t.chiSquareTest(expected, observed,0.05); // 결과과
        System.out.println(pval1);
        System.out.println(pval2);
        System.out.println("여부"+ pval3);

        long[][] expected22 = new long[][]
                {
                        {23,21,63},
                        {31,48,159},
                        {13,23,119},
                };

        double v1 = t.chiSquareTest(expected22);
        double v2 = t.chiSquare(expected22);
        boolean v3 = t.chiSquareTest(expected22,0.05);
        System.out.println("여러 행 결과 : " +v1);
        System.out.println("여러 행 결과 : " +v2);
        System.out.println("여러 행 결과 : " +v3);

//        // 남여 의 경우
//
//        long[] expected1 = new long[]
//                {
//                        50 , 125, 90, 45
//                };
//        long[] expected2 = new long[]
//                {
//                        75 , 175, 30, 10
//                };
//        double pval4 = t.chiSquareDataSetsComparison(expected1,expected2); //2개 성별일 경우 계ㄱ산
//        System.out.println("pval4 = " + pval4);
//
//        double pval5 = t.chiSquareTestDataSetsComparison(expected1, expected2);
//        boolean pval6 = t.chiSquareTestDataSetsComparison(expected1, expected2,0.05);
//        System.out.println("pval5 = " + pval5);
//        System.out.println("pval6 = " + pval6);
//
//        ChiSquaredDistribution c1 = new ChiSquaredDistribution(4,6.5);
//        System.out.println("c1.getNumericalMean() = " + c1.getNumericalMean());
//        System.out.println("c1.getNumericalMean() = " + c1);
//        System.out.println("c1.getNumericalMean() = " + c1.getDegreesOfFreedom());
//        System.out.println("c1.getNumericalMean() = " + c1.density(0.05));
//        System.out.println("c1.getNumericalMean() = " + c1.getSupportLowerBound());
//        System.out.println("c1.getNumericalMean() = " + c1.cumulativeProbability(0.05));
//
    }
}