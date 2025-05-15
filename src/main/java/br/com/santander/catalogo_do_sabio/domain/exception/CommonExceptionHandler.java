package br.com.santander.catalogo_do_sabio.domain.exception;

import br.com.santander.catalogo_do_sabio.domain.model.error.CommonError;
import br.com.santander.catalogo_do_sabio.domain.model.error.DataNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
@Slf4j
public class CommonExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<CommonError> objectNotFound(DataNotFoundException exception, HttpServletRequest request) {

        CommonError error = new CommonError(
                HttpStatus.NOT_FOUND.value(),
                "Data not found",
                exception.getMessage()
        );

        log.error("ExceptionHandler DataNotFound: {}", exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonError> unexpectedException(Exception exception, HttpServletRequest request) {

        CommonError error = new CommonError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Unexpected error",
                exception.getMessage()
        );

        log.error("ExceptionHandler InternalServerError: {}", exception.getMessage());

        return ResponseEntity.internalServerError().body(error);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<CommonError> badRequestException(Exception exception, HttpServletRequest request) {

        CommonError error = new CommonError(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                exception.getMessage()
        );

        log.error("ExceptionHandler BadRequest: {}", exception.getMessage());

        return ResponseEntity.badRequest().body(error);
    }
}
