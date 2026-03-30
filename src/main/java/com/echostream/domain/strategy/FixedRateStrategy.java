package com.echostream.domain.strategy;

import java.math.BigDecimal;

public class FixedRateStrategy implements RoyaltyEngine {

    private final BigDecimal amountPerStream;

    public FixedRateStrategy(BigDecimal amountPerStream) {
        this.amountPerStream = amountPerStream;
    }

    @Override
    public BigDecimal calculateRoyalty(RoyaltyCalculationInput input) {
        return amountPerStream.multiply(BigDecimal.valueOf(input.getStreamCount()));
    }
}
