package com.echostream.controller.mvc;

import com.echostream.domain.audio.AudioTrack;
import com.echostream.repository.AudioTrackRepository;
import com.echostream.service.AuditService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuditQueueController {

    private final AudioTrackRepository audioTrackRepository;
    private final AuditService auditService;

    public AuditQueueController(AudioTrackRepository audioTrackRepository, AuditService auditService) {
        this.audioTrackRepository = audioTrackRepository;
        this.auditService = auditService;
    }

    @GetMapping("/audit-queue")
    public String view(Model model) {
        List<AudioTrack> pendingTracks = audioTrackRepository.findByLifecycleState("PENDING_AUDIT");
        model.addAttribute("pendingTracks", pendingTracks);
        return "audit-queue";
    }

    @PostMapping("/audit-queue/{trackId}/approve")
    public String approve(@PathVariable Long trackId, RedirectAttributes redirectAttributes) {
        auditService.approveTrack(trackId);
        redirectAttributes.addFlashAttribute("message", "Track approved: " + trackId);
        return "redirect:/audit-queue";
    }

    @PostMapping("/audit-queue/{trackId}/reject")
    public String reject(@PathVariable Long trackId, RedirectAttributes redirectAttributes) {
        auditService.rejectTrack(trackId);
        redirectAttributes.addFlashAttribute("message", "Track rejected: " + trackId);
        return "redirect:/audit-queue";
    }
}
