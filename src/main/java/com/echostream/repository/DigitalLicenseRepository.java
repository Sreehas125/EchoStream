package com.echostream.repository;

import com.echostream.domain.entity.DigitalLicense;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DigitalLicenseRepository extends JpaRepository<DigitalLicense, Long> {

	List<DigitalLicense> findByAudioTrackId(Long audioTrackId);

	Optional<DigitalLicense> findTopByAudioTrackIdOrderByEffectiveUntilDesc(Long audioTrackId);
}
