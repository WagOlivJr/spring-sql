package com.ltp.gradesubmission.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericMessageException extends RuntimeException{

    private HttpStatus status;

    public GenericMessageException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    
}
