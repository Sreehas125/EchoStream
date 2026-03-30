package com.echostream.domain.factory;

import com.echostream.domain.enums.TrackType;

public final class AudioTrackFactoryProvider {

    private AudioTrackFactoryProvider() {
    }

    public static AudioTrackFactory getFactory(TrackType trackType) {
        if (trackType == TrackType.MUSIC) {
            return new MusicTrackFactory();
        }
        if (trackType == TrackType.PODCAST) {
            return new PodcastTrackFactory();
        }
        throw new IllegalArgumentException("Unsupported track type: " + trackType);
    }
}
