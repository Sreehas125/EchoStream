package com.echostream.controller.dto;

import com.echostream.domain.adapter.ExternalPodcastMetadata;

public class ExternalPodcastIngestionRequest {

    private Long creatorId;
    private String isrcCode;
    private ExternalPodcastMetadata metadata;

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getIsrcCode() {
        return isrcCode;
    }

    public void setIsrcCode(String isrcCode) {
        this.isrcCode = isrcCode;
    }

    public ExternalPodcastMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ExternalPodcastMetadata metadata) {
        this.metadata = metadata;
    }
}
