package br.com.santander.catalogo_do_sabio.domain.service;

import br.com.santander.catalogo_do_sabio.domain.model.UserApi;
import br.com.santander.catalogo_do_sabio.infrastructure.repository.UserApiRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserApiRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    private UserApi userApi;

    @BeforeEach
    void setUp() {
        userApi = new UserApi();
        userApi.setUsername("testuser");
        userApi.setPassword("password123");
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        userApi.setRoles(roles);
    }

    @Test
    void testLoadUserByUsername() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userApi));

        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("password123", result.getPassword());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void testLoadUserByUsernameShouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("nonexistent")
        );
        verify(userRepository).findByUsername("nonexistent");
    }

    @Test
    void testLoadUserByUsernameShouldReturnUserWithMultipleRoles() {
        List<String> multipleRoles = new ArrayList<>();
        multipleRoles.add("USER");
        multipleRoles.add("ADMIN");
        userApi.setRoles(multipleRoles);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userApi));

        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        assertNotNull(result);
        assertEquals(2, result.getAuthorities().size());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testLoadUserByUsernameShouldReturnUserWithNoRoles() {
        userApi.setRoles(new ArrayList<>());
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userApi));

        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        assertNotNull(result);
        assertEquals(0, result.getAuthorities().size());
    }

    @Test
    void testLoadUserByUsernameShouldPreserveUserCredentials() {
        String encodedPassword = "$2a$10$abcdefghijklmnopqrstuvwxyz";
        userApi.setPassword(encodedPassword);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userApi));

        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        assertNotNull(result);
        assertEquals(encodedPassword, result.getPassword());
    }
}