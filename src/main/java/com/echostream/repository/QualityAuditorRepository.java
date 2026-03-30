package com.echostream.repository;

import com.echostream.domain.entity.QualityAuditor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QualityAuditorRepository extends JpaRepository<QualityAuditor, Long> {
}
