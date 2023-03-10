package ir.jimsa.LovelyRecipes.config.exception;

import ir.jimsa.LovelyRecipes.shared.MyApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.Date;

@ControllerAdvice
public class AppExceptionHandler {

    Logger logger = LoggerFactory.getLogger(AppExceptionHandler.class);

    @ExceptionHandler(value = {SystemServiceException.class})
    public ResponseEntity<Object> handleRelayServiceException(SystemServiceException systemServiceException) {
        MyApiResponse response = new MyApiResponse(
                false,
                systemServiceException.getMessage(),
                new Date(),
                new ArrayList<>()
        );
        return new ResponseEntity<>(response, systemServiceException.getHttpStatus());
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleException(Exception exception) {
        logger.info("Exception had just happened, {}", exception.getMessage());
        MyApiResponse response = new MyApiResponse(
                false,
                exception.getMessage(),
                new Date(),
                new ArrayList<>()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
