package com.echostream.service;

import com.echostream.domain.audio.AudioTrack;
import com.echostream.domain.entity.ContentCreator;
import com.echostream.domain.entity.PaymentTransaction;
import com.echostream.domain.enums.PaymentStatus;
import com.echostream.domain.strategy.FixedRateStrategy;
import com.echostream.domain.strategy.RevenueShareStrategy;
import com.echostream.domain.strategy.RoyaltyCalculationInput;
import com.echostream.domain.strategy.RoyaltyStrategyContext;
import com.echostream.repository.AudioTrackRepository;
import com.echostream.repository.ContentCreatorRepository;
import com.echostream.repository.PaymentTransactionRepository;
import com.echostream.service.dto.RoyaltyCalculationCommand;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoyaltyService {

    private final AudioTrackRepository audioTrackRepository;
    private final ContentCreatorRepository contentCreatorRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;

    public RoyaltyService(
        AudioTrackRepository audioTrackRepository,
        ContentCreatorRepository contentCreatorRepository,
        PaymentTransactionRepository paymentTransactionRepository
    ) {
        this.audioTrackRepository = audioTrackRepository;
        this.contentCreatorRepository = contentCreatorRepository;
        this.paymentTransactionRepository = paymentTransactionRepository;
    }

    public BigDecimal calculateWithFixedRate(RoyaltyCalculationCommand command) {
        RoyaltyCalculationInput input = buildInput(command);
        RoyaltyStrategyContext context = new RoyaltyStrategyContext(
            new FixedRateStrategy(command.getFixedRatePerStream())
        );
        return context.calculate(input);
    }

    public BigDecimal calculateWithRevenueShare(RoyaltyCalculationCommand command) {
        RoyaltyCalculationInput input = buildInput(command);
        RoyaltyStrategyContext context = new RoyaltyStrategyContext(
            new RevenueShareStrategy(command.getRevenueSharePercent())
        );
        return context.calculate(input);
    }

    @Transactional
    public PaymentTransaction createPaymentTransactionForTrackCreator(Long trackId, BigDecimal amount, String currency) {
        AudioTrack track = audioTrackRepository
            .findWithCreatorById(trackId)
            .orElseThrow(() -> new ResourceNotFoundException("AudioTrack not found: " + trackId));

        return persistPaymentTransaction(track, track.getCreator(), amount, currency);
    }

    @Transactional
    public PaymentTransaction createPaymentTransaction(Long trackId, Long beneficiaryId, BigDecimal amount, String currency) {
        AudioTrack track = audioTrackRepository
            .findWithCreatorById(trackId)
            .orElseThrow(() -> new ResourceNotFoundException("AudioTrack not found: " + trackId));

        ContentCreator beneficiary = contentCreatorRepository
            .findById(beneficiaryId)
            .orElseThrow(() -> new ResourceNotFoundException("ContentCreator not found: " + beneficiaryId));

        return persistPaymentTransaction(track, beneficiary, amount, currency);
    }

    private PaymentTransaction persistPaymentTransaction(
        AudioTrack track,
        ContentCreator beneficiary,
        BigDecimal amount,
        String currency
    ) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payout amount must be greater than zero.");
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency is required.");
        }

        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setTransactionRef("TXN-" + UUID.randomUUID());
        transaction.setAmount(amount);
        transaction.setCurrency(currency.trim().toUpperCase());
        transaction.setStatus(PaymentStatus.INITIATED);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setAudioTrack(track);
        transaction.setBeneficiary(beneficiary);

        track.getPayments().add(transaction);
        return paymentTransactionRepository.save(transaction);
    }

    private RoyaltyCalculationInput buildInput(RoyaltyCalculationCommand command) {
        RoyaltyCalculationInput input = new RoyaltyCalculationInput();
        input.setGrossRevenue(command.getGrossRevenue());
        input.setStreamCount(command.getStreamCount());
        return input;
    }
}
