package com.echostream.controller.mvc;

import com.echostream.domain.audio.AudioTrack;
import com.echostream.domain.enums.TrackType;
import com.echostream.repository.AudioTrackRepository;
import com.echostream.repository.ContentCreatorRepository;
import com.echostream.service.AudioIngestionService;
import com.echostream.service.dto.IngestionCommand;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class IngestionController {

    private final AudioIngestionService audioIngestionService;
    private final AudioTrackRepository audioTrackRepository;
    private final ContentCreatorRepository contentCreatorRepository;

    public IngestionController(
        AudioIngestionService audioIngestionService,
        AudioTrackRepository audioTrackRepository,
        ContentCreatorRepository contentCreatorRepository
    ) {
        this.audioIngestionService = audioIngestionService;
        this.audioTrackRepository = audioTrackRepository;
        this.contentCreatorRepository = contentCreatorRepository;
    }

    @GetMapping("/ingestion-dashboard")
    public String showDashboard(Model model) {
        List<AudioTrack> tracks = audioTrackRepository.findAll();
        model.addAttribute("tracks", tracks);
        model.addAttribute("creators", contentCreatorRepository.findAll());
        model.addAttribute("ingestionForm", new IngestionForm());
        return "ingestion-dashboard";
    }

    @PostMapping("/ingestion-dashboard")
    public String ingestTrack(@ModelAttribute IngestionForm form, RedirectAttributes redirectAttributes) {
        IngestionCommand command = new IngestionCommand();
        command.setTrackType(form.getTrackType());
        command.setCreatorId(form.getCreatorId());
        command.setTitle(form.getTitle());
        command.setDurationSeconds(form.getDurationSeconds());
        command.setFilePath(form.getFilePath());
        command.setAlbumName(form.getAlbumName());
        command.setGenre(form.getGenre());
        command.setIsrcCode(form.getIsrcCode());
        command.setPodcastName(form.getPodcastName());
        command.setEpisodeNumber(form.getEpisodeNumber());
        command.setHostName(form.getHostName());

        audioIngestionService.ingestTrack(command);
        redirectAttributes.addFlashAttribute("message", "Track uploaded and queued for validation.");
        return "redirect:/ingestion-dashboard";
    }

    public static class IngestionForm {

        private TrackType trackType = TrackType.MUSIC;
        private Long creatorId;
        private String title;
        private Integer durationSeconds;
        private String filePath;
        private String albumName;
        private String genre;
        private String isrcCode;
        private String podcastName;
        private Integer episodeNumber;
        private String hostName;

        public TrackType getTrackType() {
            return trackType;
        }

        public void setTrackType(TrackType trackType) {
            this.trackType = trackType;
        }

        public Long getCreatorId() {
            return creatorId;
        }

        public void setCreatorId(Long creatorId) {
            this.creatorId = creatorId;
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
}
