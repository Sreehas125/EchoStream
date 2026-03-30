package com.echostream.domain.state;

import com.echostream.domain.enums.TrackLifecycleStatus;

public interface AudioTrackLifecycleState {

    TrackLifecycleStatus status();

    default TrackLifecycleStatus validate() {
        throw new InvalidStateTransitionException("Cannot move from " + status() + " to VALIDATING");
    }

    default TrackLifecycleStatus markPendingAudit() {
        throw new InvalidStateTransitionException("Cannot move from " + status() + " to PENDING_AUDIT");
    }

    default TrackLifecycleStatus release() {
        throw new InvalidStateTransitionException("Cannot move from " + status() + " to RELEASED");
    }

    default TrackLifecycleStatus reject() {
        throw new InvalidStateTransitionException("Cannot move from " + status() + " to REJECTED");
    }
}
