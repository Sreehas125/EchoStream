package com.echostream.domain.entity;

import com.echostream.domain.audio.AudioTrack;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "content_creators")
public class ContentCreator extends User {

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AudioTrack> uploadedTracks = new ArrayList<>();

    public List<AudioTrack> getUploadedTracks() {
        return uploadedTracks;
    }

    public void setUploadedTracks(List<AudioTrack> uploadedTracks) {
        this.uploadedTracks = uploadedTracks;
    }
}
