package cityBloxx.test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.BigDecimal.*;

/**
 * 统计最大的合法建造方法
 *
 * @author zkk
 */
public class CountMaxLegality {

    private static BigDecimal maxCaseCount = new BigDecimal(4).pow(25);

    public static void main(String[] args) {
        for (int i = 0; i < 25; i++) {
            i++;
        }
        kegalityP();
    }

    /**
     * 合法概率
     */
    private static void kegalityP() {
        //蓝色合法概率
        BigDecimal blueP = BigDecimal.valueOf(1 / 4.0);
        //红色合法概率
        BigDecimal redMiddle = BigDecimal.valueOf(9 / 25.0).multiply(ONE.subtract((BigDecimal.valueOf(3 / 4.0).pow(4))));
        BigDecimal redCorner = BigDecimal.valueOf(4 / 25.0).multiply(ONE.subtract((BigDecimal.valueOf(3 / 4.0).pow(2))));
        BigDecimal redSide = BigDecimal.valueOf(12 / 25.0).multiply(ONE.subtract((BigDecimal.valueOf(3 / 4.0).pow(3))));
        BigDecimal redP = BigDecimal.valueOf(1 / 4.0).multiply(redMiddle.add(redCorner).add(redSide));
        //绿色合法概率
        BigDecimal noRedAndNoBlue = BigDecimal.valueOf(1 / 2.0).pow(4);
        BigDecimal onlyExistsRedOrOnlyExistsBlue = BigDecimal.valueOf(3 / 4.0).pow(4).multiply(BigDecimal.valueOf(2));
        BigDecimal greenMiddle = BigDecimal.valueOf(9 / 25.0).multiply(ONE.subtract(noRedAndNoBlue.add(onlyExistsRedOrOnlyExistsBlue)));
        BigDecimal greenCorner = BigDecimal.valueOf(4 / 25.0).multiply(BigDecimal.valueOf(2 / 16.0));

        noRedAndNoBlue = BigDecimal.valueOf(1 / 2.0).pow(3);
        onlyExistsRedOrOnlyExistsBlue = BigDecimal.valueOf(3 / 4.0).pow(3).multiply(BigDecimal.valueOf(2));
        BigDecimal greenSide = BigDecimal.valueOf(12 / 25.0).multiply(ONE.subtract(noRedAndNoBlue.add(onlyExistsRedOrOnlyExistsBlue)));
        BigDecimal greenP = BigDecimal.valueOf(1 / 4.0).multiply(greenMiddle.add(greenCorner).add(greenSide));

        //金色合法概率
        BigDecimal noGoldOneColorDouble = BigDecimal.valueOf(6).multiply(BigDecimal.valueOf(2)).multiply(BigDecimal.valueOf(3));
        BigDecimal allColor = BigDecimal.valueOf(24);
        BigDecimal goldMiddle = BigDecimal.valueOf(9 / 25.0).multiply((noGoldOneColorDouble.add(allColor)).divide((BigDecimal.valueOf(4).pow(4))));
        BigDecimal goldCorner = BigDecimal.valueOf(4 / 25.0).multiply(ZERO);
        BigDecimal goldSide = BigDecimal.valueOf(12 / 25.0).multiply(BigDecimal.valueOf(6).divide((BigDecimal.valueOf(4).pow(3))));
        BigDecimal goldP = BigDecimal.valueOf(1 / 4.0).multiply(goldMiddle.add(goldCorner).add(goldSide));

        BigDecimal legalityP = blueP.add(redP).add(greenP).add(goldP);
        System.out.println(legalityP);
        BigDecimal P = (blueP.multiply(TEN).add(redP.multiply(TEN.add(TEN))).add(greenP.multiply(TEN.add(TEN).add(TEN))).add(goldP.multiply(new BigDecimal(40)))).divide(legalityP, 5, RoundingMode.HALF_EVEN);
        System.out.println(P);
        System.out.println(P.multiply(new BigDecimal(25)));
        System.out.println(maxCaseCount.multiply(legalityP));
    }
}
