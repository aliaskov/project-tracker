package com.telran.project.tracker.controller;

import com.telran.project.tracker.exception.AuthenticationException;
import com.telran.project.tracker.exception.PasswordsDoNotMatchException;
import com.telran.project.tracker.model.entity.ProjectUser;
import com.telran.project.tracker.model.entity.ProjectUserSession;
import com.telran.project.tracker.model.web.LoginRequest;
import com.telran.project.tracker.model.web.LoginResponse;
import com.telran.project.tracker.model.web.RegistrationRequest;
import com.telran.project.tracker.repository.ProjectUserRepository;
import com.telran.project.tracker.repository.ProjectUserSessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/user")
public class EntryController {

    @Autowired
    private ProjectUserSessionRepository projectUserSessionRepository;

    @Autowired
    private ProjectUserRepository userRepository;

    @PostMapping("/register")
    public void register(@RequestBody RegistrationRequest request) {

        if (!request.getPassword().equals(request.getRepeatPassword())) {
            throw new PasswordsDoNotMatchException();
        }

        ProjectUser projectUser = ProjectUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .password(request.getPassword())
                .build();

        userRepository.save(projectUser);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {

        ProjectUser projectUser = userRepository.findByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
        if (projectUser == null) {
            log.warn("Incorrect username or password. Incoming parameters are: {} - {}", loginRequest.getUsername(), loginRequest.getPassword());
            throw new AuthenticationException("Username or password is incorrect");
        }

        ProjectUserSession projectUserSession = ProjectUserSession.builder()
                .sessionId(UUID.randomUUID().toString())
                .projectUser(projectUser)
                .isValid(true)
                .build();

        projectUserSessionRepository.save(projectUserSession);
        return LoginResponse
                .builder()
                .token(projectUserSession.getSessionId())
                .build();
    }

    @PutMapping("/logout")
    public void logout(@RequestHeader("Authorization") String header) {

        ProjectUserSession projectUserSession = projectUserSessionRepository.findBySessionIdAndIsValidTrue(header);
        projectUserSession.setIsValid(false);
        projectUserSessionRepository.save(projectUserSession);
    }
}
