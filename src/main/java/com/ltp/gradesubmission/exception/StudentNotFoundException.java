package com.ltp.gradesubmission.exception;

public class StudentNotFoundException extends RuntimeException { 

    public StudentNotFoundException(Long id) {
        super("The student " + id + " does not exist in our records.");
    }

    public StudentNotFoundException(String message) {
        super(message);
    }
    
}