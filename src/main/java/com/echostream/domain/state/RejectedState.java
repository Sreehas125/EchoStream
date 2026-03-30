package com.echostream.domain.state;

import com.echostream.domain.enums.TrackLifecycleStatus;

public class RejectedState implements AudioTrackLifecycleState {

    @Override
    public TrackLifecycleStatus status() {
        return TrackLifecycleStatus.REJECTED;
    }
}
