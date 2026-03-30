package com.echostream.service.dto;

import java.math.BigDecimal;

public class RoyaltyCalculationCommand {

    private Long audioTrackId;
    private Long beneficiaryId;
    private BigDecimal grossRevenue;
    private long streamCount;
    private BigDecimal fixedRatePerStream;
    private BigDecimal revenueSharePercent;

    public Long getAudioTrackId() {
        return audioTrackId;
    }

    public void setAudioTrackId(Long audioTrackId) {
        this.audioTrackId = audioTrackId;
    }

    public Long getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(Long beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

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

    public BigDecimal getFixedRatePerStream() {
        return fixedRatePerStream;
    }

    public void setFixedRatePerStream(BigDecimal fixedRatePerStream) {
        this.fixedRatePerStream = fixedRatePerStream;
    }

    public BigDecimal getRevenueSharePercent() {
        return revenueSharePercent;
    }

    public void setRevenueSharePercent(BigDecimal revenueSharePercent) {
        this.revenueSharePercent = revenueSharePercent;
    }
}
