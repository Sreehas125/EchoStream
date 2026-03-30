package com.echostream.service;

import com.echostream.domain.audio.AudioTrack;
import com.echostream.domain.entity.QualityAuditor;
import com.echostream.domain.state.AudioTrackStateContext;
import com.echostream.repository.AudioTrackRepository;
import com.echostream.repository.QualityAuditorRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    private final AudioTrackRepository audioTrackRepository;
    private final QualityAuditorRepository qualityAuditorRepository;

    public AuditService(
        AudioTrackRepository audioTrackRepository,
        QualityAuditorRepository qualityAuditorRepository
    ) {
        this.audioTrackRepository = audioTrackRepository;
        this.qualityAuditorRepository = qualityAuditorRepository;
    }

    public AudioTrack assignAuditor(Long trackId, Long auditorId) {
        AudioTrack track = audioTrackRepository
            .findById(trackId)
            .orElseThrow(() -> new ResourceNotFoundException("AudioTrack not found: " + trackId));
        QualityAuditor auditor = qualityAuditorRepository
            .findById(auditorId)
            .orElseThrow(() -> new ResourceNotFoundException("QualityAuditor not found: " + auditorId));

        track.setAssignedAuditor(auditor);
        return audioTrackRepository.save(track);
    }

    public AudioTrack startValidation(Long trackId) {
        AudioTrack track = getTrack(trackId);
        AudioTrackStateContext stateContext = new AudioTrackStateContext(track);
        stateContext.validate();
        return audioTrackRepository.save(track);
    }

    public AudioTrack moveToPendingAudit(Long trackId) {
        AudioTrack track = getTrack(trackId);
        AudioTrackStateContext stateContext = new AudioTrackStateContext(track);
        stateContext.markPendingAudit();
        return audioTrackRepository.save(track);
    }

    public AudioTrack approveTrack(Long trackId) {
        AudioTrack track = getTrack(trackId);
        AudioTrackStateContext stateContext = new AudioTrackStateContext(track);
        stateContext.release();
        return audioTrackRepository.save(track);
    }

    public AudioTrack rejectTrack(Long trackId) {
        AudioTrack track = getTrack(trackId);
        AudioTrackStateContext stateContext = new AudioTrackStateContext(track);
        stateContext.reject();
        return audioTrackRepository.save(track);
    }

    private AudioTrack getTrack(Long trackId) {
        return audioTrackRepository
            .findById(trackId)
            .orElseThrow(() -> new ResourceNotFoundException("AudioTrack not found: " + trackId));
    }
}
