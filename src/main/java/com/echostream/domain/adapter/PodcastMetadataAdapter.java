package com.echostream.domain.adapter;

public class PodcastMetadataAdapter implements AudioMetadataAdapter<ExternalPodcastMetadata> {

    @Override
    public UnifiedAudioMetadata adapt(ExternalPodcastMetadata sourceMetadata) {
        UnifiedAudioMetadata unified = new UnifiedAudioMetadata();
        unified.setTitle(sourceMetadata.getEpisodeTitle());
        unified.setDurationSeconds(sourceMetadata.getDurationInSeconds());
        unified.setFilePath(sourceMetadata.getStorageUrl());
        unified.setPodcastName(sourceMetadata.getShowName());
        unified.setEpisodeNumber(sourceMetadata.getEpisodeNo());
        unified.setHostName(sourceMetadata.getHost());
        return unified;
    }
}
