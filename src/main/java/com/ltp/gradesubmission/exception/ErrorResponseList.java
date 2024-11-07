package com.ltp.gradesubmission.exception;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponseList {
    
    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    // private LocalDateTime timestamp;
    private List<String> message;

    public ErrorResponseList(List<String> message) {
        this.message = message;
        // this.timestamp = LocalDateTime.now();
    }
    
}
