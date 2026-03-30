package com.echostream.controller;

import com.echostream.domain.audio.AudioTrack;
import com.echostream.service.AuditService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @PutMapping("/tracks/{trackId}/assign/{auditorId}")
    public AudioTrack assignAuditor(@PathVariable Long trackId, @PathVariable Long auditorId) {
        return auditService.assignAuditor(trackId, auditorId);
    }

    @PutMapping("/tracks/{trackId}/start-validation")
    public AudioTrack startValidation(@PathVariable Long trackId) {
        return auditService.startValidation(trackId);
    }

    @PutMapping("/tracks/{trackId}/pending-audit")
    public AudioTrack moveToPendingAudit(@PathVariable Long trackId) {
        return auditService.moveToPendingAudit(trackId);
    }

    @PutMapping("/tracks/{trackId}/approve")
    public AudioTrack approveTrack(@PathVariable Long trackId) {
        return auditService.approveTrack(trackId);
    }

    @PutMapping("/tracks/{trackId}/reject")
    public AudioTrack rejectTrack(@PathVariable Long trackId) {
        return auditService.rejectTrack(trackId);
    }
}
