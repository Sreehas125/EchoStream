package com.echostream.domain.adapter;

public class MetadataAdapterFacade {

    private final MusicMetadataAdapter musicMetadataAdapter = new MusicMetadataAdapter();
    private final PodcastMetadataAdapter podcastMetadataAdapter = new PodcastMetadataAdapter();

    // Adapter Pattern: converts multiple external metadata shapes into one unified model.
    // Why used: ingestion flow remains format-agnostic and consistent across sources.
    public UnifiedAudioMetadata fromMusicMetadata(LegacyMusicMetadata metadata) {
        return musicMetadataAdapter.adapt(metadata);
    }

    public UnifiedAudioMetadata fromPodcastMetadata(ExternalPodcastMetadata metadata) {
        return podcastMetadataAdapter.adapt(metadata);
    }
}
