package io.codelex.groupdinner.repository.service;

import io.codelex.groupdinner.MapDBRecordToApiCompatible;
import io.codelex.groupdinner.api.User;
import io.codelex.groupdinner.api.request.RegistrationRequest;
import io.codelex.groupdinner.api.request.SignInRequest;
import io.codelex.groupdinner.repository.TestVariableGenerator;
import io.codelex.groupdinner.repository.UserRecordRepository;
import io.codelex.groupdinner.repository.model.UserRecord;
import org.junit.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

public class AuthServiceTest {

    private final PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
    private final TestVariableGenerator generator = new TestVariableGenerator();
    private MapDBRecordToApiCompatible toApiCompatible = Mockito.mock(MapDBRecordToApiCompatible.class);
    private UserRecordRepository userRecordRepository = Mockito.mock(UserRecordRepository.class);
    private AuthService authService = new AuthService(userRecordRepository, passwordEncoder);
    private UserRecord userRecord1 = generator.createUserRecord1();
    private User user1 = generator.getUserFromUserRecord(1L, userRecord1);
    private RegistrationRequest registrationRequest = generator.createRegistrationRequest();
    private SignInRequest signInRequest = generator.createSignInRequest();


    @Test
    public void should_be_able_to_register_as_new_user() {
        //when
        Mockito.when(userRecordRepository.isUserPresent(any()))
                .thenReturn(false);
        String encodedPassword = "encodedPassword";
        Mockito.when(passwordEncoder.encode(any()))
                .thenReturn(encodedPassword);
        Mockito.when(userRecordRepository.save(any()))
                .thenReturn(userRecord1);
        Mockito.when(toApiCompatible.apply(userRecord1))
                .thenReturn(user1);

        User result = authService.registerUser(registrationRequest);

        //then
        assertEquals(result, user1);
    }

    @Test
    public void should_not_be_able_to_register_with_existing_email() {
        //when
        Mockito.when(userRecordRepository.isUserPresent(any()))
                .thenReturn(true);

        //then
        Executable executable = () -> authService.registerUser(registrationRequest);
        assertThrows(IllegalStateException.class, executable, "Email already exists");
    }

    @Test
    public void should_be_able_to_sign_in() {
        //when
        Mockito.when(userRecordRepository.findByEmail(any()))
                .thenReturn(userRecord1);
        Mockito.when(passwordEncoder.matches(any(), any()))
                .thenReturn(true);
        Mockito.when(toApiCompatible.apply(userRecord1))
                .thenReturn(user1);

        User result = authService.authenticateUser(signInRequest);

        //then
        assertEquals(result, user1);
    }

    @Test
    public void should_not_be_able_to_sign_in_with_wrong_password() {
        //when
        Mockito.when(userRecordRepository.findByEmail(any()))
                .thenReturn(userRecord1);
        Mockito.when(passwordEncoder.matches(any(), any()))
                .thenReturn(false);

        //then
        Executable executable = () -> authService.authenticateUser(signInRequest);
        assertThrows(IllegalStateException.class, executable, "Password incorrect");
    }

    @Test
    public void should_not_be_able_to_sign_in_with_nonexistent_email() {
        //when
        Mockito.when(userRecordRepository.findByEmail(any()))
                .thenReturn(null);

        //then
        Executable executable = () -> authService.authenticateUser(signInRequest);
        assertThrows(IllegalStateException.class, executable, "Email incorrect");
    }

}