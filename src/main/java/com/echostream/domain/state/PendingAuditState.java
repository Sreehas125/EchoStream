package com.echostream.domain.state;

import com.echostream.domain.enums.TrackLifecycleStatus;

public class PendingAuditState implements AudioTrackLifecycleState {

    @Override
    public TrackLifecycleStatus status() {
        return TrackLifecycleStatus.PENDING_AUDIT;
    }

    @Override
    public TrackLifecycleStatus release() {
        return TrackLifecycleStatus.RELEASED;
    }

    @Override
    public TrackLifecycleStatus reject() {
        return TrackLifecycleStatus.REJECTED;
    }
}
