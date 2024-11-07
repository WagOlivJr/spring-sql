package com.ltp.gradesubmission.validation;

import java.util.Arrays;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ScoreValidator implements ConstraintValidator<Score, String>{
    
        List<String> scores = Arrays.asList(
        "A+", "A", "A-",
        "B+", "B", "B-",
        "C+", "C", "C-",
        "D+", "D", "D-",
        "F"
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context){
        return scores.contains(value);
        // if(value == null) return false;
        // for(String score: scores) {
        //     if(value.equals(score)) return true;
        // }
        // return false;
    }

}
