package com.echostream.controller.mvc;

import com.echostream.domain.entity.PaymentTransaction;
import com.echostream.domain.enums.PaymentStatus;
import com.echostream.repository.PaymentTransactionRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TransactionEditsController {

    private final PaymentTransactionRepository paymentTransactionRepository;

    public TransactionEditsController(PaymentTransactionRepository paymentTransactionRepository) {
        this.paymentTransactionRepository = paymentTransactionRepository;
    }

    @GetMapping("/transaction-edits")
    public String view(@RequestParam(required = false) Long editId, Model model) {
        EditForm form = new EditForm();
        if (editId != null) {
            paymentTransactionRepository.findById(editId).ifPresent(transaction -> {
                form.setTransactionId(transaction.getId());
                form.setCurrency(transaction.getCurrency());
                form.setStatus(transaction.getStatus());
            });
        }

        populate(model, form);
        return "transaction-edits";
    }

    @PostMapping("/transaction-edits/select")
    public String selectTransaction(@RequestParam Long transactionId) {
        return "redirect:/transaction-edits?editId=" + transactionId;
    }

    @PostMapping("/transaction-edits/update")
    public String updateTransaction(@ModelAttribute EditForm form, RedirectAttributes redirectAttributes) {
        PaymentTransaction transaction = paymentTransactionRepository
            .findById(form.getTransactionId())
            .orElse(null);

        if (transaction == null) {
            redirectAttributes.addFlashAttribute("error", "Transaction not found.");
            return "redirect:/transaction-edits";
        }

        String currency = sanitizeCurrency(form.getCurrency());
        if (currency == null) {
            redirectAttributes.addFlashAttribute("error", "Currency must be a 3-letter code.");
            return "redirect:/transaction-edits?editId=" + transaction.getId();
        }
        if (form.getStatus() == null) {
            redirectAttributes.addFlashAttribute("error", "Status is required.");
            return "redirect:/transaction-edits?editId=" + transaction.getId();
        }

        transaction.setCurrency(currency);
        transaction.setStatus(form.getStatus());
        paymentTransactionRepository.save(transaction);

        redirectAttributes.addFlashAttribute("message", "Transaction updated successfully.");
        return "redirect:/transaction-edits?editId=" + transaction.getId();
    }

    private void populate(Model model, EditForm form) {
        List<PaymentTransaction> transactions = new ArrayList<>(paymentTransactionRepository.findAll());
        transactions.sort(Comparator.comparing(PaymentTransaction::getTransactionDate).reversed());

        model.addAttribute("transactions", transactions);
        model.addAttribute("editForm", form);
        model.addAttribute("statuses", PaymentStatus.values());
    }

    private String sanitizeCurrency(String currency) {
        if (currency == null) {
            return null;
        }
        String normalized = currency.trim().toUpperCase();
        if (normalized.length() != 3) {
            return null;
        }
        return normalized;
    }

    public static class EditForm {

        private Long transactionId;
        private String currency;
        private PaymentStatus status;

        public Long getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(Long transactionId) {
            this.transactionId = transactionId;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public PaymentStatus getStatus() {
            return status;
        }

        public void setStatus(PaymentStatus status) {
            this.status = status;
        }
    }
}