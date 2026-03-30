package com.echostream.domain.strategy;

import java.math.BigDecimal;

public class RoyaltyStrategyContext {

    private RoyaltyEngine royaltyEngine;

    // Strategy Pattern: algorithm is selected at runtime using RoyaltyEngine implementations.
    // Why used: royalty rules vary without changing service orchestration code.
    public RoyaltyStrategyContext(RoyaltyEngine royaltyEngine) {
        this.royaltyEngine = royaltyEngine;
    }

    public void setRoyaltyEngine(RoyaltyEngine royaltyEngine) {
        this.royaltyEngine = royaltyEngine;
    }

    public BigDecimal calculate(RoyaltyCalculationInput input) {
        return royaltyEngine.calculateRoyalty(input);
    }
}
