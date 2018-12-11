package com.telran.project.tracker.service;

import com.telran.project.tracker.model.web.LoginRequest;
import com.telran.project.tracker.model.web.LoginResponse;
import com.telran.project.tracker.model.web.RegistrationRequest;

public interface EntryService {

    void register(RegistrationRequest request);
    LoginResponse login(LoginRequest loginRequest);
    void logout(String header);
}
