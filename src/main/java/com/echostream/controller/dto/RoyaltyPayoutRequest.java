package com.echostream.controller.dto;

import java.math.BigDecimal;

public class RoyaltyPayoutRequest {

    private Long audioTrackId;
    private Long beneficiaryId;
    private BigDecimal amount;
    private String currency;

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
