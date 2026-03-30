package com.echostream.domain.factory;

import com.echostream.domain.audio.AudioTrack;
import com.echostream.domain.audio.PodcastTrack;

public class PodcastTrackFactory extends AudioTrackFactory {

    @Override
    public AudioTrack createTrack(TrackCreationRequest request) {
        // Concrete factory method for PodcastTrack creation.
        PodcastTrack track = new PodcastTrack();
        track.setTitle(request.getTitle());
        track.setDurationSeconds(request.getDurationSeconds());
        track.setFilePath(request.getFilePath());
        track.setUploadedAt(request.getUploadedAt());
        track.setCreator(request.getCreator());
        track.setPodcastName(request.getPodcastName());
        track.setEpisodeNumber(request.getEpisodeNumber());
        track.setHostName(request.getHostName());
        return track;
    }
}
