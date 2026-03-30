package com.echostream.repository;

import com.echostream.domain.audio.AudioTrack;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AudioTrackRepository extends JpaRepository<AudioTrack, Long> {

	List<AudioTrack> findByLifecycleState(String lifecycleState);
}
