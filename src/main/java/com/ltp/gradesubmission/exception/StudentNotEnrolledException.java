package com.ltp.gradesubmission.exception;

public class StudentNotEnrolledException extends RuntimeException {
    public StudentNotEnrolledException(Long studentId, Long courseId) {
        super("The student " + studentId + " is not enrolled in the course " + courseId + ".");
    }    
}
