package com.echostream.domain.factory;

import com.echostream.domain.entity.ContentCreator;
import java.time.LocalDateTime;

public class TrackCreationRequest {

    private String title;
    private Integer durationSeconds;
    private String filePath;
    private LocalDateTime uploadedAt;
    private ContentCreator creator;

    private String albumName;
    private String genre;
    private String isrcCode;

    private String podcastName;
    private Integer episodeNumber;
    private String hostName;

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

    public ContentCreator getCreator() {
        return creator;
    }

    public void setCreator(ContentCreator creator) {
        this.creator = creator;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getIsrcCode() {
        return isrcCode;
    }

    public void setIsrcCode(String isrcCode) {
        this.isrcCode = isrcCode;
    }

    public String getPodcastName() {
        return podcastName;
    }

    public void setPodcastName(String podcastName) {
        this.podcastName = podcastName;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
}
