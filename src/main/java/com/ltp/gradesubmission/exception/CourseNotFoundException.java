package com.ltp.gradesubmission.exception;

public class CourseNotFoundException extends RuntimeException { 

    public CourseNotFoundException(Long id) {
        super("The course " + id + " does not exist in our records.");
    }

    public CourseNotFoundException(String message) {
        super(message);
    }
    
}