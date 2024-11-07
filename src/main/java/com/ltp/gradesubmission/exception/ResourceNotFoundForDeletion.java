package com.ltp.gradesubmission.exception;

public class ResourceNotFoundForDeletion extends RuntimeException{
    public ResourceNotFoundForDeletion() {
        super("Cannot delete non-existing resource.");
    }
}
