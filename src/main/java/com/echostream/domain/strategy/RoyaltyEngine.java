package com.echostream.domain.strategy;

import java.math.BigDecimal;

public interface RoyaltyEngine {

    BigDecimal calculateRoyalty(RoyaltyCalculationInput input);
}
