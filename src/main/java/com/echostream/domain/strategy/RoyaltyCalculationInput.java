package com.echostream.domain.strategy;

import java.math.BigDecimal;

public class RoyaltyCalculationInput {

    private BigDecimal grossRevenue;
    private long streamCount;

    public BigDecimal getGrossRevenue() {
        return grossRevenue;
    }

    public void setGrossRevenue(BigDecimal grossRevenue) {
        this.grossRevenue = grossRevenue;
    }

    public long getStreamCount() {
        return streamCount;
    }

    public void setStreamCount(long streamCount) {
        this.streamCount = streamCount;
    }
}
