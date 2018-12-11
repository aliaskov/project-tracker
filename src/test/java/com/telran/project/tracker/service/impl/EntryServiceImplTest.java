package com.telran.project.tracker.service.impl;

import com.telran.project.tracker.exception.AuthenticationException;
import com.telran.project.tracker.model.entity.ProjectUser;
import com.telran.project.tracker.model.entity.ProjectUserSession;
import com.telran.project.tracker.model.web.LoginRequest;
import com.telran.project.tracker.model.web.LoginResponse;
import com.telran.project.tracker.model.web.RegistrationRequest;
import com.telran.project.tracker.repository.ProjectUserRepository;
import com.telran.project.tracker.repository.ProjectUserSessionRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EntryServiceImplTest {
    private static final String INCORRECT_USERNAME = "incorrect_username";
    private static final String INCORRECT_PASSWORD = "incorrect_password";

    private static final ProjectUser DEFAULT_USER =

            ProjectUser.builder()
                    .id(1L)
                    .firstName("FirstName")
                    .lastName("LastName")
                    .password("123456")
                    .username("username")
                    .build();


    private static final ProjectUserSession DEFAULT_USER_SESSION =

            ProjectUserSession.builder()
                    .projectUser(DEFAULT_USER)
                    .sessionId("random_session_id")
                    .isValid(true)
                    .id(1L)
                    .build();


    @InjectMocks
    private EntryServiceImpl entryService;

    @Mock
    private ProjectUserSessionRepository projectUserSessionRepository;

    @Mock
    private ProjectUserRepository userRepository;



    //-------------------- Registration tests --------------------

    @Test
    public void register_positive() {

        Mockito.when(userRepository.save(ArgumentMatchers.any(ProjectUser.class))).thenReturn(DEFAULT_USER);

        RegistrationRequest request = RegistrationRequest.builder()
                .firstName(DEFAULT_USER.getFirstName())
                .lastName(DEFAULT_USER.getLastName())
                .password(DEFAULT_USER.getPassword())
                .username(DEFAULT_USER.getUsername())
                .build();

        entryService.register(request);
    }


    //-------------------- Login tests --------------------

    @Test
    public void login_positive() {
        Mockito
                .when(userRepository.findByUsernameAndPassword(DEFAULT_USER.getUsername(), DEFAULT_USER.getPassword()))
                .thenReturn(DEFAULT_USER);

        Mockito
                .when(projectUserSessionRepository.save(ArgumentMatchers.any(ProjectUserSession.class)))
                .thenReturn(DEFAULT_USER_SESSION);

        LoginRequest loginRequest = LoginRequest.builder()
                .username(DEFAULT_USER.getUsername())
                .password(DEFAULT_USER.getPassword())
                .build();


        LoginResponse response = entryService.login(loginRequest);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getToken(), DEFAULT_USER_SESSION.getSessionId());
    }


    @Test(expected = AuthenticationException.class)
    public void login_incorrectUsername() {

        LoginRequest loginRequest = LoginRequest.builder()
                .username(INCORRECT_USERNAME)
                .password(DEFAULT_USER.getPassword())
                .build();


        entryService.login(loginRequest);
    }

    @Test(expected = AuthenticationException.class)
    public void login_incorrectPassword() {

        LoginRequest loginRequest = LoginRequest.builder()
                .username(DEFAULT_USER.getUsername())
                .password(INCORRECT_PASSWORD)
                .build();


        entryService.login(loginRequest);
    }

    @Test(expected = AuthenticationException.class)
    public void login_incorrectUsernameAndPassword() {

        LoginRequest loginRequest = LoginRequest.builder()
                .username(INCORRECT_USERNAME)
                .password(INCORRECT_PASSWORD)
                .build();

        entryService.login(loginRequest);
    }


    //-------------------- Logout tests --------------------

    @Test
    public void logout_positive() {
        Mockito.when(projectUserSessionRepository.findBySessionIdAndIsValidTrue(DEFAULT_USER_SESSION.getSessionId())).thenReturn(DEFAULT_USER_SESSION);

        Mockito.when(projectUserSessionRepository.save(ArgumentMatchers.any(ProjectUserSession.class))).thenReturn(DEFAULT_USER_SESSION);

        entryService.logout(DEFAULT_USER_SESSION.getSessionId());
    }

    @Test
    public void logout_incorrect_session_id() {
        Mockito.when(projectUserSessionRepository.findBySessionIdAndIsValidTrue(ArgumentMatchers.anyString())).thenReturn(null);

        entryService.logout("somehow_false_session_id");
    }

    @Test
    public void logout_invalidated_session_id() {

        final ProjectUserSession tempSession = ProjectUserSession.builder()
                .sessionId(DEFAULT_USER_SESSION.getSessionId())
                .projectUser(DEFAULT_USER_SESSION.getProjectUser())
                .id(DEFAULT_USER_SESSION.getId())
                .isValid(false)
                .build();

        Mockito.when(projectUserSessionRepository.findBySessionIdAndIsValidTrue(ArgumentMatchers.anyString())).thenReturn(tempSession);

        entryService.logout("somehow_false_session_id");
    }
}