package com.kaylaarthur.financeapi.service;

import com.kaylaarthur.financeapi.model.User;
import com.kaylaarthur.financeapi.repository.UserRepo;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    
    private UserService userService;
    private UserRepo userRepo;
    private BCryptPasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        userRepo = mock(UserRepo.class);
        encoder = new BCryptPasswordEncoder();
        userService = new UserService(userRepo, encoder);
    } // setUp

    @Test
    void shouldCreateUserSuccessfully() {

        String name = "Misty";
        String email = "mistythemaster@test.com";
        String password = "m1Sty7qT";

        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());

        User savedUser = new User(name, email, "hashed");
        savedUser.setId(1L);

        when(userRepo.save(any(User.class))).thenReturn(savedUser);
        User result = userService.createUser(name, email, password);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(email, result.getEmail());
        verify(userRepo).save(any(User.class));
    } // shouldCreateUserSuccessfully


    @Test
    void shouldThrowExceptionWhenEmailExists() {
        String email = "kayla@test.com";

        when(userRepo.findByEmail(email))
            .thenReturn(Optional.of(new User("Exists", email,"hashed")));

            assertThrows(IllegalArgumentException.class, () -> {
                userService.createUser("NewUser", email, "newUserPass");
            });

            verify(userRepo, never()).save(any());
    } // shouldThrowExceptionWhenEmailExists

    @Test
    void shouldHashPassword() {
        when(userRepo.findByEmail(any())).thenReturn(Optional.empty());

        when(userRepo.save(any(User.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

            User user = userService.createUser(("HashTest"), "testHash@gmail.com", "hashpass123");

            assertTrue(encoder.matches("hashpass123", user.getHashedPassword()));
    } // shouldHashPassword
    
} // UserServiceTest
