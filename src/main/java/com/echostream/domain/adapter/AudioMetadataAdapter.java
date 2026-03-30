package com.echostream.domain.adapter;

public interface AudioMetadataAdapter<T> {

    UnifiedAudioMetadata adapt(T sourceMetadata);
}
