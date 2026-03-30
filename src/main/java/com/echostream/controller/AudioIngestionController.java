package com.echostream.controller;

import com.echostream.controller.dto.ExternalPodcastIngestionRequest;
import com.echostream.controller.dto.IngestionRequest;
import com.echostream.controller.dto.LegacyMusicIngestionRequest;
import com.echostream.domain.audio.AudioTrack;
import com.echostream.domain.enums.TrackType;
import com.echostream.service.AudioIngestionService;
import com.echostream.service.dto.IngestionCommand;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ingestion")
public class AudioIngestionController {

    private final AudioIngestionService audioIngestionService;

    public AudioIngestionController(AudioIngestionService audioIngestionService) {
        this.audioIngestionService = audioIngestionService;
    }

    @PostMapping("/tracks")
    @ResponseStatus(HttpStatus.CREATED)
    public AudioTrack ingestTrack(@RequestBody IngestionRequest request) {
        IngestionCommand command = mapIngestionCommand(request);
        return audioIngestionService.ingestTrack(command);
    }

    @PostMapping("/legacy/music")
    @ResponseStatus(HttpStatus.CREATED)
    public AudioTrack ingestLegacyMusic(@RequestBody LegacyMusicIngestionRequest request) {
        IngestionCommand command = new IngestionCommand();
        command.setTrackType(TrackType.MUSIC);
        command.setCreatorId(request.getCreatorId());
        command.setIsrcCode(request.getIsrcCode());
        return audioIngestionService.ingestLegacyMusic(request.getMetadata(), command);
    }

    @PostMapping("/external/podcast")
    @ResponseStatus(HttpStatus.CREATED)
    public AudioTrack ingestExternalPodcast(@RequestBody ExternalPodcastIngestionRequest request) {
        IngestionCommand command = new IngestionCommand();
        command.setTrackType(TrackType.PODCAST);
        command.setCreatorId(request.getCreatorId());
        command.setIsrcCode(request.getIsrcCode());
        return audioIngestionService.ingestExternalPodcast(request.getMetadata(), command);
    }

    private IngestionCommand mapIngestionCommand(IngestionRequest request) {
        IngestionCommand command = new IngestionCommand();
        command.setTrackType(request.getTrackType());
        command.setCreatorId(request.getCreatorId());
        command.setTitle(request.getTitle());
        command.setDurationSeconds(request.getDurationSeconds());
        command.setFilePath(request.getFilePath());
        command.setAlbumName(request.getAlbumName());
        command.setGenre(request.getGenre());
        command.setIsrcCode(request.getIsrcCode());
        command.setPodcastName(request.getPodcastName());
        command.setEpisodeNumber(request.getEpisodeNumber());
        command.setHostName(request.getHostName());
        return command;
    }
}
