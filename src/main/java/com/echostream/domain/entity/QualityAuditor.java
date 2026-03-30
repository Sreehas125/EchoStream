package com.echostream.domain.entity;

import com.echostream.domain.audio.AudioTrack;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quality_auditors")
public class QualityAuditor extends User {

    @OneToMany(mappedBy = "assignedAuditor", fetch = FetchType.LAZY)
    private List<AudioTrack> assignedTracks = new ArrayList<>();

    public List<AudioTrack> getAssignedTracks() {
        return assignedTracks;
    }

    public void setAssignedTracks(List<AudioTrack> assignedTracks) {
        this.assignedTracks = assignedTracks;
    }
}
