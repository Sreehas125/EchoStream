package com.echostream.controller.dto;

import com.echostream.domain.adapter.LegacyMusicMetadata;

public class LegacyMusicIngestionRequest {

    private Long creatorId;
    private String isrcCode;
    private LegacyMusicMetadata metadata;

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

    public LegacyMusicMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(LegacyMusicMetadata metadata) {
        this.metadata = metadata;
    }
}
