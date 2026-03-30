package com.echostream.service;

import com.echostream.domain.audio.AudioTrack;
import com.echostream.domain.entity.DigitalLicense;
import com.echostream.domain.enums.LicenseType;
import com.echostream.repository.AudioTrackRepository;
import com.echostream.repository.DigitalLicenseRepository;
import com.echostream.service.dto.RightsCommand;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class RightsManagementService {

    private final AudioTrackRepository audioTrackRepository;
    private final DigitalLicenseRepository digitalLicenseRepository;

    public RightsManagementService(
        AudioTrackRepository audioTrackRepository,
        DigitalLicenseRepository digitalLicenseRepository
    ) {
        this.audioTrackRepository = audioTrackRepository;
        this.digitalLicenseRepository = digitalLicenseRepository;
    }

    public DigitalLicense issueLicense(Long trackId, RightsCommand command) {
        AudioTrack track = audioTrackRepository
            .findById(trackId)
            .orElseThrow(() -> new ResourceNotFoundException("AudioTrack not found: " + trackId));

        DigitalLicense license = new DigitalLicense();
        license.setLicenseCode(command.getLicenseCode());
        license.setLicenseType(command.getLicenseType());
        license.setTerritory(command.getTerritory());
        license.setEffectiveFrom(command.getEffectiveFrom());
        license.setEffectiveUntil(command.getEffectiveUntil());
        license.setAudioTrack(track);

        track.getLicenses().add(license);
        return digitalLicenseRepository.save(license);
    }

    public DigitalLicense renewLicense(Long trackId) {
        RightsCommand renewal = new RightsCommand();
        renewal.setLicenseCode("RENEW-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        renewal.setLicenseType(LicenseType.NON_EXCLUSIVE);
        renewal.setTerritory("GLOBAL");
        renewal.setEffectiveFrom(LocalDate.now());
        renewal.setEffectiveUntil(LocalDate.now().plusYears(1));
        return issueLicense(trackId, renewal);
    }

    public void revokeAccess(Long trackId) {
        List<DigitalLicense> licenses = digitalLicenseRepository.findByAudioTrackId(trackId);
        for (DigitalLicense license : licenses) {
            license.setEffectiveUntil(LocalDate.now());
        }
        digitalLicenseRepository.saveAll(licenses);
    }
}
