package com.telran.project.tracker;

import com.telran.project.tracker.model.entity.ProjectUser;
import com.telran.project.tracker.repository.ProjectUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    @Autowired
    private ProjectUserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        ProjectUser projectUser = ProjectUser.builder()
        .username("johnd")
        .password("123456")
        .firstName("John")
        .lastName("Dale")
        .build();

        userRepository.save(projectUser);
    }
}
