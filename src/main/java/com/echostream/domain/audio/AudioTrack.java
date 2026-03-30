package com.echostream.domain.audio;

import com.echostream.domain.entity.ContentCreator;
import com.echostream.domain.entity.DigitalLicense;
import com.echostream.domain.entity.PaymentTransaction;
import com.echostream.domain.entity.QualityAuditor;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "audio_tracks")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AudioTrack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 180)
    private String title;

    @Column(nullable = false)
    private Integer durationSeconds;

    @Column(nullable = false, length = 300)
    private String filePath;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    @Column(nullable = false, length = 40)
    private String lifecycleState = "UPLOADED";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private ContentCreator creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auditor_id")
    private QualityAuditor assignedAuditor;

    @OneToMany(mappedBy = "audioTrack", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DigitalLicense> licenses = new ArrayList<>();

    @OneToMany(mappedBy = "audioTrack", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentTransaction> payments = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getLifecycleState() {
        return lifecycleState;
    }

    public void setLifecycleState(String lifecycleState) {
        this.lifecycleState = lifecycleState;
    }

    public ContentCreator getCreator() {
        return creator;
    }

    public void setCreator(ContentCreator creator) {
        this.creator = creator;
    }

    public QualityAuditor getAssignedAuditor() {
        return assignedAuditor;
    }

    public void setAssignedAuditor(QualityAuditor assignedAuditor) {
        this.assignedAuditor = assignedAuditor;
    }

    public List<DigitalLicense> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<DigitalLicense> licenses) {
        this.licenses = licenses;
    }

    public List<PaymentTransaction> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentTransaction> payments) {
        this.payments = payments;
    }
}
