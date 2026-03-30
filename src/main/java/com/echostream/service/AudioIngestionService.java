package com.echostream.service;

import com.echostream.domain.adapter.ExternalPodcastMetadata;
import com.echostream.domain.adapter.LegacyMusicMetadata;
import com.echostream.domain.adapter.MetadataAdapterFacade;
import com.echostream.domain.adapter.UnifiedAudioMetadata;
import com.echostream.domain.audio.AudioTrack;
import com.echostream.domain.entity.ContentCreator;
import com.echostream.domain.enums.TrackType;
import com.echostream.domain.factory.AudioTrackFactory;
import com.echostream.domain.factory.AudioTrackFactoryProvider;
import com.echostream.domain.factory.TrackCreationRequest;
import com.echostream.repository.AudioTrackRepository;
import com.echostream.repository.ContentCreatorRepository;
import com.echostream.service.dto.IngestionCommand;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class AudioIngestionService {

    private final AudioTrackRepository audioTrackRepository;
    private final ContentCreatorRepository contentCreatorRepository;
    private final MetadataAdapterFacade metadataAdapterFacade = new MetadataAdapterFacade();

    public AudioIngestionService(
        AudioTrackRepository audioTrackRepository,
        ContentCreatorRepository contentCreatorRepository
    ) {
        this.audioTrackRepository = audioTrackRepository;
        this.contentCreatorRepository = contentCreatorRepository;
    }

    public AudioTrack ingestTrack(IngestionCommand command) {
        ContentCreator creator = contentCreatorRepository
            .findById(command.getCreatorId())
            .orElseThrow(() -> new ResourceNotFoundException("ContentCreator not found: " + command.getCreatorId()));

        TrackCreationRequest request = new TrackCreationRequest();
        request.setTitle(command.getTitle());
        request.setDurationSeconds(command.getDurationSeconds());
        request.setFilePath(command.getFilePath());
        request.setUploadedAt(LocalDateTime.now());
        request.setCreator(creator);

        request.setAlbumName(command.getAlbumName());
        request.setGenre(command.getGenre());
        request.setIsrcCode(command.getIsrcCode());

        request.setPodcastName(command.getPodcastName());
        request.setEpisodeNumber(command.getEpisodeNumber());
        request.setHostName(command.getHostName());

        AudioTrackFactory factory = AudioTrackFactoryProvider.getFactory(command.getTrackType());
    AudioTrack track = factory.createTrack(request);
    return audioTrackRepository.save(track);
    }

    public AudioTrack ingestLegacyMusic(LegacyMusicMetadata metadata, IngestionCommand command) {
        UnifiedAudioMetadata unifiedMetadata = metadataAdapterFacade.fromMusicMetadata(metadata);
        return ingestFromUnifiedMetadata(TrackType.MUSIC, unifiedMetadata, command);
    }

    public AudioTrack ingestExternalPodcast(ExternalPodcastMetadata metadata, IngestionCommand command) {
        UnifiedAudioMetadata unifiedMetadata = metadataAdapterFacade.fromPodcastMetadata(metadata);
        return ingestFromUnifiedMetadata(TrackType.PODCAST, unifiedMetadata, command);
    }

    private AudioTrack ingestFromUnifiedMetadata(
        TrackType trackType,
        UnifiedAudioMetadata metadata,
        IngestionCommand command
    ) {
        IngestionCommand normalized = new IngestionCommand();
        normalized.setTrackType(trackType);
        normalized.setCreatorId(command.getCreatorId());
        normalized.setTitle(metadata.getTitle());
        normalized.setDurationSeconds(metadata.getDurationSeconds());
        normalized.setFilePath(metadata.getFilePath());

        normalized.setAlbumName(metadata.getAlbumName());
        normalized.setGenre(metadata.getGenre());
        normalized.setIsrcCode(command.getIsrcCode());

        normalized.setPodcastName(metadata.getPodcastName());
        normalized.setEpisodeNumber(metadata.getEpisodeNumber());
        normalized.setHostName(metadata.getHostName());

        return ingestTrack(normalized);
    }
}
