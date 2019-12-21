package com.exercise.loyalty.component;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class PointsCalculator {
    private static final BigDecimal THRESHOLD_1 = BigDecimal.valueOf(5001);
    private static final BigDecimal THRESHOLD_2 = BigDecimal.valueOf(7501);
    private static final BigDecimal MULTIPLIER_1 = BigDecimal.ONE;
    private static final BigDecimal MULTIPLIER_2 = BigDecimal.valueOf(2);
    private static final BigDecimal MULTIPLIER_3 = BigDecimal.valueOf(3);
    private static final BigDecimal AVAILABLE_POINTS_MULTIPLIER = BigDecimal.valueOf(100);

    public BigDecimal calculatePendingPoints(BigDecimal cashAmount)
    {
        BigDecimal cashAmountInt = cashAmount.setScale(0, RoundingMode.DOWN);
        if (cashAmountInt.compareTo(THRESHOLD_1) < 0)
        {
            return cashAmountInt.multiply(MULTIPLIER_1);
        }
        if (cashAmountInt.compareTo(THRESHOLD_2) < 0)
        {
            BigDecimal level2Amount = cashAmountInt.subtract(THRESHOLD_1).add(BigDecimal.ONE);
            return getLevel1Points().add(level2Amount.multiply(MULTIPLIER_2));
        }

        BigDecimal level3Amount = cashAmountInt.subtract(THRESHOLD_2).add(BigDecimal.ONE);
        return getLevel1Points()
                .add(getLevel2Points())
                .add(level3Amount.multiply(MULTIPLIER_3));
    }

    public BigDecimal calculateAvailablePointsSpent(BigDecimal cashAmount)
    {
        BigDecimal cashAmountRounded = cashAmount.setScale(2, RoundingMode.DOWN);
        return cashAmountRounded.multiply(AVAILABLE_POINTS_MULTIPLIER);
    }

    private BigDecimal getLevel1Points()
    {
        return THRESHOLD_1.subtract(BigDecimal.ONE).multiply(MULTIPLIER_1);
    }

    private BigDecimal getLevel2Points()
    {
        return THRESHOLD_2.subtract(THRESHOLD_1).multiply(MULTIPLIER_2);
    }
}
