package com.echostream.controller.mvc;

import com.echostream.domain.entity.PaymentTransaction;
import com.echostream.repository.PaymentTransactionRepository;
import com.echostream.service.RoyaltyService;
import com.echostream.service.dto.RoyaltyCalculationCommand;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RoyaltyReportController {

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final RoyaltyService royaltyService;

    public RoyaltyReportController(
        PaymentTransactionRepository paymentTransactionRepository,
        RoyaltyService royaltyService
    ) {
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.royaltyService = royaltyService;
    }

    @GetMapping("/royalty-report")
    public String view(Model model) {
        populateDashboard(model, new RoyaltyViewForm(), null, null, null);
        return "royalty-report";
    }

    @PostMapping("/royalty-report")
    public String calculate(@ModelAttribute RoyaltyViewForm form, Model model) {
        try {
            RoyaltyCalculationCommand command = new RoyaltyCalculationCommand();
            command.setAudioTrackId(form.getAudioTrackId());
            command.setStreamCount(form.getStreamCount());
            command.setGrossRevenue(orZero(form.getGrossRevenue()));
            command.setFixedRatePerStream(orZero(form.getFixedRatePerStream()));
            command.setRevenueSharePercent(orZero(form.getRevenueSharePercent()));

            BigDecimal payout = "FIXED".equalsIgnoreCase(form.getMode())
                ? royaltyService.calculateWithFixedRate(command)
                : royaltyService.calculateWithRevenueShare(command);

            royaltyService.createPaymentTransactionForTrackCreator(form.getAudioTrackId(), payout, "USD");
            populateDashboard(model, form, payout, "Payment transaction created for this payout.", null);
        } catch (RuntimeException ex) {
            String errorMessage = ex.getMessage() == null || ex.getMessage().isBlank()
                ? "Unable to calculate payout and create transaction."
                : ex.getMessage();
            populateDashboard(model, form, null, null, errorMessage);
        }
        return "royalty-report";
    }

    private BigDecimal orZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private void populateDashboard(
        Model model,
        RoyaltyViewForm form,
        BigDecimal payout,
        String successMessage,
        String errorMessage
    ) {
        List<PaymentTransaction> transactions = paymentTransactionRepository.findAll();
        BigDecimal totalPaid = transactions
            .stream()
            .map(PaymentTransaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("transactions", transactions);
        model.addAttribute("totalPaid", totalPaid);
        model.addAttribute("transactionCount", transactions.size());
        model.addAttribute("royaltyForm", form);
        model.addAttribute("calculatedPayout", payout);
        model.addAttribute("message", successMessage);
        model.addAttribute("error", errorMessage);
    }

    public static class RoyaltyViewForm {

        private Long audioTrackId;
        private long streamCount;
        private BigDecimal grossRevenue = BigDecimal.ZERO;
        private BigDecimal fixedRatePerStream = BigDecimal.ZERO;
        private BigDecimal revenueSharePercent = BigDecimal.ZERO;
        private String mode = "FIXED";

        public Long getAudioTrackId() {
            return audioTrackId;
        }

        public void setAudioTrackId(Long audioTrackId) {
            this.audioTrackId = audioTrackId;
        }

        public long getStreamCount() {
            return streamCount;
        }

        public void setStreamCount(long streamCount) {
            this.streamCount = streamCount;
        }

        public BigDecimal getGrossRevenue() {
            return grossRevenue;
        }

        public void setGrossRevenue(BigDecimal grossRevenue) {
            this.grossRevenue = grossRevenue;
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

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }
    }
}
