package com.echostream.domain.adapter;

public class MusicMetadataAdapter implements AudioMetadataAdapter<LegacyMusicMetadata> {

    @Override
    public UnifiedAudioMetadata adapt(LegacyMusicMetadata sourceMetadata) {
        UnifiedAudioMetadata unified = new UnifiedAudioMetadata();
        unified.setTitle(sourceMetadata.getSongTitle());
        unified.setDurationSeconds(sourceMetadata.getLengthSeconds());
        unified.setFilePath(sourceMetadata.getFileLocation());
        unified.setAlbumName(sourceMetadata.getAlbum());
        unified.setGenre(sourceMetadata.getGenreName());
        return unified;
    }
}
