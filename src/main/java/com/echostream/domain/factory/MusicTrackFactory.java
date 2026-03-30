package com.echostream.domain.factory;

import com.echostream.domain.audio.AudioTrack;
import com.echostream.domain.audio.MusicTrack;

public class MusicTrackFactory extends AudioTrackFactory {

    @Override
    public AudioTrack createTrack(TrackCreationRequest request) {
        // Concrete factory method for MusicTrack creation.
        MusicTrack track = new MusicTrack();
        track.setTitle(request.getTitle());
        track.setDurationSeconds(request.getDurationSeconds());
        track.setFilePath(request.getFilePath());
        track.setUploadedAt(request.getUploadedAt());
        track.setCreator(request.getCreator());
        track.setAlbumName(request.getAlbumName());
        track.setGenre(request.getGenre());
        track.setIsrcCode(request.getIsrcCode());
        return track;
    }
}
