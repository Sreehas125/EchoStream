package com.echostream.controller.mvc;

import com.echostream.domain.audio.AudioTrack;
import com.echostream.service.ResourceNotFoundException;
import com.echostream.repository.AudioTrackRepository;
import com.echostream.service.AuditService;
import java.util.ArrayList;
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
        List<AudioTrack> inboundTracks = new ArrayList<>();
        inboundTracks.addAll(audioTrackRepository.findByLifecycleState("UPLOADED"));
        inboundTracks.addAll(audioTrackRepository.findByLifecycleState("VALIDATING"));

        List<AudioTrack> pendingTracks = audioTrackRepository.findByLifecycleState("PENDING_AUDIT");
        model.addAttribute("inboundTracks", inboundTracks);
        model.addAttribute("pendingTracks", pendingTracks);
        return "audit-queue";
    }

    @PostMapping("/audit-queue/{trackId}/queue")
    public String queueForAudit(@PathVariable Long trackId, RedirectAttributes redirectAttributes) {
        AudioTrack track = audioTrackRepository
            .findById(trackId)
            .orElseThrow(() -> new ResourceNotFoundException("AudioTrack not found: " + trackId));

        String state = track.getLifecycleState();
        if ("UPLOADED".equals(state)) {
            auditService.startValidation(trackId);
            auditService.moveToPendingAudit(trackId);
            redirectAttributes.addFlashAttribute("message", "Track moved to pending audit: " + trackId);
            return "redirect:/audit-queue";
        }

        if ("VALIDATING".equals(state)) {
            auditService.moveToPendingAudit(trackId);
            redirectAttributes.addFlashAttribute("message", "Track moved to pending audit: " + trackId);
            return "redirect:/audit-queue";
        }

        if ("PENDING_AUDIT".equals(state)) {
            redirectAttributes.addFlashAttribute("message", "Track is already in pending audit: " + trackId);
            return "redirect:/audit-queue";
        }

        redirectAttributes.addFlashAttribute("error", "Track cannot be queued from state: " + state);
        return "redirect:/audit-queue";
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
