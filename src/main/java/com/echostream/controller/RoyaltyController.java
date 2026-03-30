package com.echostream.controller;

import com.echostream.controller.dto.RoyaltyPayoutRequest;
import com.echostream.domain.entity.PaymentTransaction;
import com.echostream.service.RoyaltyService;
import com.echostream.service.dto.RoyaltyCalculationCommand;
import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/royalties")
public class RoyaltyController {

    private final RoyaltyService royaltyService;

    public RoyaltyController(RoyaltyService royaltyService) {
        this.royaltyService = royaltyService;
    }

    @PostMapping("/calculate/fixed-rate")
    public BigDecimal calculateFixedRate(@RequestBody RoyaltyCalculationCommand command) {
        return royaltyService.calculateWithFixedRate(command);
    }

    @PostMapping("/calculate/revenue-share")
    public BigDecimal calculateRevenueShare(@RequestBody RoyaltyCalculationCommand command) {
        return royaltyService.calculateWithRevenueShare(command);
    }

    @PostMapping("/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentTransaction createTransaction(@RequestBody RoyaltyPayoutRequest request) {
        return royaltyService.createPaymentTransaction(
            request.getAudioTrackId(),
            request.getBeneficiaryId(),
            request.getAmount(),
            request.getCurrency()
        );
    }
}
