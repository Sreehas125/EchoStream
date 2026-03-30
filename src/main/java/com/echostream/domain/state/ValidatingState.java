package com.echostream.domain.state;

import com.echostream.domain.enums.TrackLifecycleStatus;

public class ValidatingState implements AudioTrackLifecycleState {

    @Override
    public TrackLifecycleStatus status() {
        return TrackLifecycleStatus.VALIDATING;
    }

    @Override
    public TrackLifecycleStatus markPendingAudit() {
        return TrackLifecycleStatus.PENDING_AUDIT;
    }

    @Override
    public TrackLifecycleStatus reject() {
        return TrackLifecycleStatus.REJECTED;
    }
}
