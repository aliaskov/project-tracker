package com.telran.project.tracker.model.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

@Entity
@Table(name = "PROJECT_USER_SESSION")
public class ProjectUserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "SESSION_ID", unique = true)
    private String sessionId;

    @Column(name = "IS_VALID")
    private Boolean isValid;

    @OneToOne
    @JoinColumn(name = "PROJECT_USER_ID")
    private ProjectUser projectUser;

}
