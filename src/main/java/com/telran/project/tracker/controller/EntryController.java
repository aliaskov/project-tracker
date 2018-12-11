package com.telran.project.tracker.controller;

import com.telran.project.tracker.exception.PasswordsDoNotMatchException;
import com.telran.project.tracker.model.web.LoginRequest;
import com.telran.project.tracker.model.web.LoginResponse;
import com.telran.project.tracker.model.web.RegistrationRequest;
import com.telran.project.tracker.service.EntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class EntryController {

    @Autowired
    private EntryService entryService;

    @PostMapping("/register")
    public void register(@RequestBody RegistrationRequest request) {

        if (!request.getPassword().equals(request.getRepeatPassword())) {
            throw new PasswordsDoNotMatchException();
        }

        entryService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return entryService.login(loginRequest);
    }

    @PutMapping("/logout")
    public void logout(@RequestHeader("Authorization") String header) {

        entryService.logout(header);
    }
}
