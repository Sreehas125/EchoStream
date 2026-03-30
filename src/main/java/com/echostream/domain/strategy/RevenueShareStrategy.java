package com.echostream.domain.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RevenueShareStrategy implements RoyaltyEngine {

    private final BigDecimal sharePercentage;

    public RevenueShareStrategy(BigDecimal sharePercentage) {
        this.sharePercentage = sharePercentage;
    }

    @Override
    public BigDecimal calculateRoyalty(RoyaltyCalculationInput input) {
        BigDecimal shareRatio = sharePercentage.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
        return input.getGrossRevenue().multiply(shareRatio);
    }
}
