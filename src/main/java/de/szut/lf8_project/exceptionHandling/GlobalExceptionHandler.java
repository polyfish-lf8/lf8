package de.szut.lf8_project.exceptionHandling;

import de.szut.lf8_project.utils.HTTPCodes;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "the given resource was not found",
                    content = @Content)
    })
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleHelloEntityNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = HTTPCodes.BAD_REQUEST, description = "The data provided by the client is wrong or incomplete",
                    content = @Content)
    })
    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<?> handleLf8ProjectInvalidDataException(InvalidDataException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
