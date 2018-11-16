package com.telran.project.tracker.repository;

import com.telran.project.tracker.model.entity.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {

    ProjectUser findByUsernameAndPassword(String username, String password);
    ProjectUser findByUsername(String username);
}
