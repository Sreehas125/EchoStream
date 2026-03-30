package com.echostream.domain.state;

import com.echostream.domain.enums.TrackLifecycleStatus;

public class ReleasedState implements AudioTrackLifecycleState {

    @Override
    public TrackLifecycleStatus status() {
        return TrackLifecycleStatus.RELEASED;
    }
}
