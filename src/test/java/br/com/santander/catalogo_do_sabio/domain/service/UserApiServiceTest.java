package br.com.santander.catalogo_do_sabio.domain.service;

import br.com.santander.catalogo_do_sabio.domain.model.UserApi;
import br.com.santander.catalogo_do_sabio.domain.model.error.DataNotFoundException;
import br.com.santander.catalogo_do_sabio.infrastructure.repository.UserApiRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserApiServiceTest {

    @Mock
    private UserApiRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserApiService userService;

    private UserApi user;

    @BeforeEach
    void setUp() {
        user = new UserApi();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setUsername("testuser");
        user.setBooksSeen(new LinkedList<>());
    }

    @Test
    void testCreateUser() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserApi.class))).thenReturn(user);

        UserApi result = userService.createUser(user);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(UserApi.class));
    }

    @Test
    void testCreateUserShouldThrowExceptionWhenUsernameExists() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        assertThrows(ResponseStatusException.class, () ->
                userService.createUser(user)
        );
        verify(userRepository).findByUsername("testuser");
        verify(userRepository, never()).save(any(UserApi.class));
    }

    @Test
    void testFindUserByUsername() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        UserApi result = userService.findUserByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void testFindUserByUsernameShouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () ->
                userService.findUserByUsername("nonexistent")
        );
        verify(userRepository).findByUsername("nonexistent");
    }

    @Test
    void testUpdateUser() {
        UserApi existingUser = new UserApi();
        existingUser.setUsername("testuser");
        existingUser.setBooksSeen(new LinkedList<>());

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(UserApi.class))).thenReturn(existingUser);

        UserApi result = userService.updateUser(user);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository).save(any(UserApi.class));
    }

    @Test
    void testUpdateUserShouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () ->
                userService.updateUser(user)
        );
        verify(userRepository).findByUsername("testuser");
        verify(userRepository, never()).save(any(UserApi.class));
    }

    @Test
    void testGetCurrentUser() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserApi result = userService.getCurrentUser();

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository).findByUsername("testuser");
    }
}