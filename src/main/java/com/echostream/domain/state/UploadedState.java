package com.echostream.domain.state;

import com.echostream.domain.enums.TrackLifecycleStatus;

public class UploadedState implements AudioTrackLifecycleState {

    @Override
    public TrackLifecycleStatus status() {
        return TrackLifecycleStatus.UPLOADED;
    }

    @Override
    public TrackLifecycleStatus validate() {
        return TrackLifecycleStatus.VALIDATING;
    }
}
