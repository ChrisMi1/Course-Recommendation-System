package com.uniwa.course_recommendation.controller;

import com.google.gson.JsonSyntaxException;
import com.uniwa.course_recommendation.exception.KeyNotFound;
import com.uniwa.course_recommendation.exception.RecommenderApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class AppExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<String> handleException(KeyNotFound keyNotFound){
        return new ResponseEntity<>(keyNotFound.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<String> handleException(RecommenderApiException apiException){
        return new ResponseEntity<>(apiException.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(JsonSyntaxException jsonSyntaxException){
        return new ResponseEntity<>(jsonSyntaxException.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(IOException ioException){
        return new ResponseEntity<>(ioException.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }
    @ExceptionHandler
    public ResponseEntity<String> handleException(InterruptedException interruptedException){
        return new ResponseEntity<>(interruptedException.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }


}
