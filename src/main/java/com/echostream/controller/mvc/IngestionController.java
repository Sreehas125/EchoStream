package com.echostream.controller.mvc;

import com.echostream.domain.audio.AudioTrack;
import com.echostream.domain.enums.TrackType;
import com.echostream.repository.AudioTrackRepository;
import com.echostream.repository.ContentCreatorRepository;
import com.echostream.service.AudioIngestionService;
import com.echostream.service.LocalFileStorageService;
import com.echostream.service.dto.IngestionCommand;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class IngestionController {

    private static final long MUSIC_MAX_FILE_SIZE_BYTES = 10L * 1024 * 1024;
    private static final long PODCAST_MAX_FILE_SIZE_BYTES = 100L * 1024 * 1024;

    private final AudioIngestionService audioIngestionService;
    private final LocalFileStorageService localFileStorageService;
    private final AudioTrackRepository audioTrackRepository;
    private final ContentCreatorRepository contentCreatorRepository;

    public IngestionController(
        AudioIngestionService audioIngestionService,
        LocalFileStorageService localFileStorageService,
        AudioTrackRepository audioTrackRepository,
        ContentCreatorRepository contentCreatorRepository
    ) {
        this.audioIngestionService = audioIngestionService;
        this.localFileStorageService = localFileStorageService;
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
        String validationError = validateUpload(form.getTrackType(), form.getUploadFile());
        if (validationError != null) {
            redirectAttributes.addFlashAttribute("error", validationError);
            return "redirect:/ingestion-dashboard";
        }

        String storedPath = localFileStorageService.store(form.getUploadFile());
        IngestionCommand command = new IngestionCommand();
        command.setTrackType(form.getTrackType());
        command.setCreatorId(form.getCreatorId());
        command.setTitle(form.getTitle());
        command.setDurationSeconds(form.getDurationSeconds());
        command.setFilePath(storedPath);
        command.setAlbumName(form.getAlbumName());
        command.setGenre(form.getGenre());
        command.setIsrcCode(form.getIsrcCode());
        command.setPodcastName(form.getPodcastName());
        command.setEpisodeNumber(form.getEpisodeNumber());
        command.setHostName(form.getHostName());

        try {
            audioIngestionService.ingestTrack(command);
        } catch (RuntimeException ex) {
            localFileStorageService.deleteIfExists(storedPath);
            throw ex;
        }
        redirectAttributes.addFlashAttribute("message", "Track uploaded and queued for validation.");
        return "redirect:/ingestion-dashboard";
    }

    private String validateUpload(TrackType trackType, MultipartFile uploadFile) {
        if (trackType == null) {
            return "Track type is required.";
        }
        if (uploadFile == null || uploadFile.isEmpty()) {
            return "Please choose a local audio file to upload.";
        }

        long maxSize = trackType == TrackType.PODCAST ? PODCAST_MAX_FILE_SIZE_BYTES : MUSIC_MAX_FILE_SIZE_BYTES;
        if (uploadFile.getSize() > maxSize) {
            String limit = trackType == TrackType.PODCAST ? "100 MB" : "10 MB";
            return "Selected file exceeds the " + limit + " limit for " + trackType + ".";
        }

        return null;
    }

    public static class IngestionForm {

        private TrackType trackType = TrackType.MUSIC;
        private Long creatorId;
        private String title;
        private Integer durationSeconds;
        private MultipartFile uploadFile;
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

        public MultipartFile getUploadFile() {
            return uploadFile;
        }

        public void setUploadFile(MultipartFile uploadFile) {
            this.uploadFile = uploadFile;
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
