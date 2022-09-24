package likelion.ylw.calculator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;

@SpringBootTest
public class CalculatorTest {

    @Test
    public void calc1(){
        ChiSquaredDistribution x2 = new ChiSquaredDistribution(3);

        System.out.println(x2.getDegreesOfFreedom());
        System.out.println(x2.getNumericalMean());
        System.out.println(x2.getNumericalVariance());
        System.out.println(x2.getSupportLowerBound());
        System.out.println(x2.getSupportUpperBound());
        System.out.println(x2.density(0.25));
    }
}
