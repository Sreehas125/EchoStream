package com.echostream.domain.factory;

import com.echostream.domain.audio.AudioTrack;

public abstract class AudioTrackFactory {

    // Factory Method Pattern: concrete creators return the specific AudioTrack subtype.
    // Why used: decouples track creation logic from callers and avoids direct constructors.
    public abstract AudioTrack createTrack(TrackCreationRequest request);
}
