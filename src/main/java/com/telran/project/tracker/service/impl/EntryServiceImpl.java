package com.telran.project.tracker.service.impl;

import com.telran.project.tracker.exception.AuthenticationException;
import com.telran.project.tracker.model.entity.ProjectUser;
import com.telran.project.tracker.model.entity.ProjectUserSession;
import com.telran.project.tracker.model.web.LoginRequest;
import com.telran.project.tracker.model.web.LoginResponse;
import com.telran.project.tracker.model.web.RegistrationRequest;
import com.telran.project.tracker.repository.ProjectUserRepository;
import com.telran.project.tracker.repository.ProjectUserSessionRepository;
import com.telran.project.tracker.service.EntryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class EntryServiceImpl implements EntryService {


    @Autowired
    private ProjectUserSessionRepository projectUserSessionRepository;

    @Autowired
    private ProjectUserRepository userRepository;

    @Override
    public void register(RegistrationRequest request) {
        ProjectUser projectUser = ProjectUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .password(request.getPassword())
                .build();

        userRepository.save(projectUser);

        log.info("New user created");
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        ProjectUser projectUser = userRepository.findByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
        if (projectUser == null) {
            log.warn("Incorrect username or password. Incoming parameters are: {} - {}", loginRequest.getUsername(), loginRequest.getPassword());
            throw new AuthenticationException("Username or password is incorrect");
        }

        ProjectUserSession projectUserSession = projectUserSessionRepository.save(ProjectUserSession.builder()
                                                                                                    .sessionId(UUID.randomUUID().toString())
                                                                                                    .projectUser(projectUser)
                                                                                                    .isValid(true)
                                                                                                    .build());


        log.debug("User with username = {} logged in", projectUser.getUsername());
        return LoginResponse
                .builder()
                .token(projectUserSession.getSessionId())
                .build();
    }

    @Override
    public void logout(String header) {
        ProjectUserSession projectUserSession = projectUserSessionRepository.findBySessionIdAndIsValidTrue(header);

        if (projectUserSession == null) {
            log.warn("Logout call - no session ID found = {}, after passing security filter. Exiting without an error", header);
            return;
        }

        projectUserSession.setIsValid(false);
        projectUserSessionRepository.save(projectUserSession);
        log.debug("Session ID = {} invalidated", header);
    }
}
