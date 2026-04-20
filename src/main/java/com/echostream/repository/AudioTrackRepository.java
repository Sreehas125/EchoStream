package com.echostream.repository;

import com.echostream.domain.audio.AudioTrack;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AudioTrackRepository extends JpaRepository<AudioTrack, Long> {

	List<AudioTrack> findByLifecycleState(String lifecycleState);

	@Query("select t from AudioTrack t join fetch t.creator where t.id = :id")
	Optional<AudioTrack> findWithCreatorById(@Param("id") Long id);
}
