package com.echostream.domain.state;

import com.echostream.domain.audio.AudioTrack;
import com.echostream.domain.enums.TrackLifecycleStatus;

public class AudioTrackStateContext {

    private final AudioTrack audioTrack;
    private AudioTrackLifecycleState currentState;

    // State Pattern: lifecycle behavior changes based on current state object.
    // Why used: enforces valid transitions for Uploaded -> Validating -> PendingAudit -> Released or Rejected.
    public AudioTrackStateContext(AudioTrack audioTrack) {
        this.audioTrack = audioTrack;
        this.currentState = fromStatus(TrackLifecycleStatus.valueOf(audioTrack.getLifecycleState()));
    }

    public TrackLifecycleStatus currentStatus() {
        return currentState.status();
    }

    public void validate() {
        transitionTo(currentState.validate());
    }

    public void markPendingAudit() {
        transitionTo(currentState.markPendingAudit());
    }

    public void release() {
        transitionTo(currentState.release());
    }

    public void reject() {
        transitionTo(currentState.reject());
    }

    private void transitionTo(TrackLifecycleStatus newStatus) {
        audioTrack.setLifecycleState(newStatus.name());
        currentState = fromStatus(newStatus);
    }

    private AudioTrackLifecycleState fromStatus(TrackLifecycleStatus status) {
        return switch (status) {
            case UPLOADED -> new UploadedState();
            case VALIDATING -> new ValidatingState();
            case PENDING_AUDIT -> new PendingAuditState();
            case RELEASED -> new ReleasedState();
            case REJECTED -> new RejectedState();
        };
    }
}
