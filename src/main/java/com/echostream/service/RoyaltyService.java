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

    public PaymentTransaction createPaymentTransaction(Long trackId, Long beneficiaryId, BigDecimal amount, String currency) {
        AudioTrack track = audioTrackRepository
            .findById(trackId)
            .orElseThrow(() -> new ResourceNotFoundException("AudioTrack not found: " + trackId));

        ContentCreator beneficiary = contentCreatorRepository
            .findById(beneficiaryId)
            .orElseThrow(() -> new ResourceNotFoundException("ContentCreator not found: " + beneficiaryId));

        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setTransactionRef("TXN-" + UUID.randomUUID());
        transaction.setAmount(amount);
        transaction.setCurrency(currency);
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
