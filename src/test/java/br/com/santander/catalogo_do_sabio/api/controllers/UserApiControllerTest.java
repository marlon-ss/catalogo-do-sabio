package br.com.santander.catalogo_do_sabio.api.controllers;

import br.com.santander.catalogo_do_sabio.api.dto.UserApiDTO;
import br.com.santander.catalogo_do_sabio.domain.model.UserApi;
import br.com.santander.catalogo_do_sabio.domain.service.UserApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserApiControllerTest {

    @Mock
    private UserApiService userService;

    @InjectMocks
    private UserApiController userController;

    private UserApi testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserApi(
                "testUser",
                "password123",
                Arrays.asList("ROLE_USER", "ROLE_ADMIN")
        );
    }

    @Test
    void testRegisterUser() {
        when(userService.createUser(any(UserApi.class))).thenReturn(testUser);

        ResponseEntity<UserApiDTO> response = userController.registerUser(testUser);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());

        UserApiDTO responseDTO = response.getBody();
        assertEquals(testUser.getUsername(), responseDTO.getUsername());
        assertEquals(testUser.getRoles(), responseDTO.getRoles());

        verify(userService, times(1)).createUser(testUser);
    }

    @Test
    void testGetUserByUsername() {
        String username = "testUser";
        when(userService.findUserByUsername(username)).thenReturn(testUser);

        ResponseEntity<UserApiDTO> response = userController.registerUser(username);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());

        UserApiDTO responseDTO = response.getBody();
        assertEquals(testUser.getUsername(), responseDTO.getUsername());
        assertEquals(testUser.getRoles(), responseDTO.getRoles());

        verify(userService, times(1)).findUserByUsername(username);
    }

    @Test
    void testConvertUserToDTO() {
        when(userService.createUser(any(UserApi.class))).thenReturn(testUser);

        ResponseEntity<UserApiDTO> response = userController.registerUser(testUser);
        UserApiDTO dto = response.getBody();

        assertNotNull(dto);
        assertEquals(testUser.getUsername(), dto.getUsername());
        assertEquals(testUser.getRoles(), dto.getRoles());
    }
}