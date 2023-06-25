package com.pjatk.nbp.project.errorHandling;

import com.pjatk.nbp.project.service.DateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestControllerAdvice extends ResponseEntityExceptionHandler {

    @Autowired
    DateService dateService;
    @ExceptionHandler(value = HttpClientErrorException.NotFound.class)
    protected ResponseEntity<Object> handleNotFound(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "[RestControllerAdvice][ERROR] " + dateService.getTime() + " " + request + " failed with [404 Not Found] exception!";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = HttpClientErrorException.BadRequest.class)
    protected ResponseEntity<Object> handleBadRequest(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "[RestControllerAdvice][ERROR] " + dateService.getTime() + " " + request + " failed with [400 Bad Request] exception!";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = HttpClientErrorException.TooManyRequests.class)
    protected ResponseEntity<Object> handleBadRequestLimitExceeded(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "[RestControllerAdvice][ERROR] " + dateService.getTime() + " " + request + " failed with [400 Bad Request - przekroczony limit] exception!";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.TOO_MANY_REQUESTS, request);
    }
}
