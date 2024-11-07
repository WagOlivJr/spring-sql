package com.ltp.gradesubmission.exception;

public class GradeNotFoundException extends RuntimeException { 

    public GradeNotFoundException(Long studentId, Long courseId) {
        super("The grade with student " + studentId + " and course " + courseId + " does not exist in our records.");
    }

    public GradeNotFoundException(String message) {
        super(message);
    }
    
}