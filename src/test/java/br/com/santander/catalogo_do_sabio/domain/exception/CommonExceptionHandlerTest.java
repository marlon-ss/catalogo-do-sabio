package br.com.santander.catalogo_do_sabio.domain.exception;

import br.com.santander.catalogo_do_sabio.domain.model.error.CommonError;
import br.com.santander.catalogo_do_sabio.domain.model.error.DataNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommonExceptionHandlerTest {

    @InjectMocks
    private CommonExceptionHandler exceptionHandler;

    @Mock
    private HttpServletRequest request;

    @Test
    void testDataNotFoundShouldReturnNotFoundStatus() {
        String errorMessage = "Recurso não encontrado";
        DataNotFoundException exception = new DataNotFoundException(errorMessage);

        ResponseEntity<CommonError> response = exceptionHandler.dataNotFound(exception, request);
        CommonError error = response.getBody();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
        assertNotNull(error);
        assertEquals(HttpStatus.NOT_FOUND.value(), error.getStatus());
        assertEquals("Data not found", error.getError());
        assertEquals(errorMessage, error.getMessage());
    }

    @Test
    void testUnexpectedExceptionShouldReturnInternalServerError() {
        String errorMessage = "Erro inesperado";
        Exception exception = new Exception(errorMessage);

        ResponseEntity<CommonError> response = exceptionHandler.unexpectedException(exception, request);
        CommonError error = response.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
        assertNotNull(error);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), error.getStatus());
        assertEquals("Unexpected error", error.getError());
        assertEquals(errorMessage, error.getMessage());
    }

    @Test
    void testBadRequestExceptionShouldReturnBadRequest() {
        String errorMessage = "Requisição inválida";
        ResponseStatusException exception = new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);

        ResponseEntity<CommonError> response = exceptionHandler.badRequestException(exception, request);
        CommonError error = response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        assertNotNull(error);
        assertEquals(HttpStatus.BAD_REQUEST.value(), error.getStatus());
        assertEquals("Bad Request", error.getError());
        assertTrue(error.getMessage().contains(errorMessage));
    }

    @Test
    void testHandleAccessDeniedExceptionShouldReturnUnauthorized() {
        String errorMessage = "Acesso negado";
        AccessDeniedException exception = new AccessDeniedException(errorMessage);

        ResponseEntity<CommonError> response = exceptionHandler.handleAccessDeniedException(exception, request);
        CommonError error = response.getBody();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCodeValue());
        assertNotNull(error);
        assertEquals(HttpStatus.UNAUTHORIZED.value(), error.getStatus());
        assertEquals("Acesso não autorizado", error.getError());
        assertEquals("Você não tem permissão para acessar este recurso", error.getMessage());
    }

    @Test
    void testResponseShouldNeverHaveNullBody() {
        Exception exception = new Exception("Teste");

        ResponseEntity<CommonError> response1 = exceptionHandler.dataNotFound(
                new DataNotFoundException("Teste"), request);
        ResponseEntity<CommonError> response2 = exceptionHandler.unexpectedException(
                exception, request);
        ResponseEntity<CommonError> response3 = exceptionHandler.badRequestException(
                new ResponseStatusException(HttpStatus.BAD_REQUEST), request);
        ResponseEntity<CommonError> response4 = exceptionHandler.handleAccessDeniedException(
                new AccessDeniedException("Teste"), request);

        assertNotNull(response1.getBody());
        assertNotNull(response2.getBody());
        assertNotNull(response3.getBody());
        assertNotNull(response4.getBody());
    }

    @Test
    void testShouldMaintainConsistentHttpStatusCodes() {
        DataNotFoundException dataNotFoundException = new DataNotFoundException("Teste");
        ResponseEntity<CommonError> response = exceptionHandler.dataNotFound(dataNotFoundException, request);

        assertEquals(response.getStatusCodeValue(), response.getBody().getStatus());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
    }
}