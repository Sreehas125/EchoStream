package com.echostream.controller.mvc;

import com.echostream.domain.audio.AudioTrack;
import com.echostream.domain.entity.DigitalLicense;
import com.echostream.repository.AudioTrackRepository;
import com.echostream.repository.DigitalLicenseRepository;
import com.echostream.service.RightsManagementService;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RightsManagementViewController {

    private final AudioTrackRepository audioTrackRepository;
    private final DigitalLicenseRepository digitalLicenseRepository;
    private final RightsManagementService rightsManagementService;

    public RightsManagementViewController(
        AudioTrackRepository audioTrackRepository,
        DigitalLicenseRepository digitalLicenseRepository,
        RightsManagementService rightsManagementService
    ) {
        this.audioTrackRepository = audioTrackRepository;
        this.digitalLicenseRepository = digitalLicenseRepository;
        this.rightsManagementService = rightsManagementService;
    }

    @GetMapping("/rights-management")
    public String view(Model model) {
        List<AudioTrack> tracks = audioTrackRepository.findAll();
        Map<Long, DigitalLicense> latestByTrack = new HashMap<>();
        for (AudioTrack track : tracks) {
            DigitalLicense latest = digitalLicenseRepository
                .findTopByAudioTrackIdOrderByEffectiveUntilDesc(track.getId())
                .orElse(null);
            latestByTrack.put(track.getId(), latest);
        }
        model.addAttribute("tracks", tracks);
        model.addAttribute("latestByTrack", latestByTrack);
        model.addAttribute("today", LocalDate.now());
        return "rights-management";
    }

    @PostMapping("/rights-management/{trackId}/renew")
    public String renew(@PathVariable Long trackId, RedirectAttributes redirectAttributes) {
        rightsManagementService.renewLicense(trackId);
        redirectAttributes.addFlashAttribute("message", "License renewed for track " + trackId);
        return "redirect:/rights-management";
    }

    @PostMapping("/rights-management/{trackId}/revoke")
    public String revoke(@PathVariable Long trackId, RedirectAttributes redirectAttributes) {
        rightsManagementService.revokeAccess(trackId);
        redirectAttributes.addFlashAttribute("message", "Access revoked for track " + trackId);
        return "redirect:/rights-management";
    }
}
