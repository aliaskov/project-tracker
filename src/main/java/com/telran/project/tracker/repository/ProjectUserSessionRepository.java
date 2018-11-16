package com.telran.project.tracker.repository;

import com.telran.project.tracker.model.entity.ProjectUserSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectUserSessionRepository extends JpaRepository<ProjectUserSession, Long> {

    ProjectUserSession findBySessionIdAndIsValidTrue(String sessionId);
}
