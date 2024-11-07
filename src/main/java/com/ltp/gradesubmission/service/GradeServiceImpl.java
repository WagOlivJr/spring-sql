package com.ltp.gradesubmission.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ltp.gradesubmission.entity.Course;
import com.ltp.gradesubmission.entity.Grade;
import com.ltp.gradesubmission.entity.Student;
import com.ltp.gradesubmission.exception.GradeNotFoundException;
import com.ltp.gradesubmission.exception.ResourceNotFoundForDeletion;
import com.ltp.gradesubmission.exception.StudentNotEnrolledException;
import com.ltp.gradesubmission.repository.CourseRepository;
// import com.ltp.gradesubmission.repository.CourseRepository;
import com.ltp.gradesubmission.repository.GradeRepository;
import com.ltp.gradesubmission.repository.StudentRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class GradeServiceImpl implements GradeService {
    
    // @Autowired
    GradeRepository gradeRepository;

    // @Autowired
    StudentRepository studentRepository;

    // @Autowired
    CourseRepository courseRepository;

    // public GradeServiceImpl(GradeRepository gradeRepository, StudentRepository studentRepository) {// este constructor dispensa a utilização de @Autowired
    // // lombok cria este constructor automaticamente, por isos apenas a notação já é suficiente para não precisar utlizar @Autowired
    //     this.gradeRepository = gradeRepository;
    //     this.studentRepository = studentRepository;
    // }

    @Override
    public Grade getGrade(Long studentId, Long courseId) {
        Optional<Grade> grade = gradeRepository.findByStudentIdAndCourseId(studentId, courseId);
        return unwrapGrade(grade, studentId, courseId);
    }

    @Override
    public Grade saveGrade(Grade grade, Long studentId, Long courseId) {
        boolean courseExists = courseRepository.existsById(courseId);
        boolean studentExists = studentRepository.existsById(studentId);
        if(courseExists && studentExists) {
        // Student student = StudentServiceImpl.unwrapStudent(studentRepository.findById(studentId), studentId);
        // Course course = CourseServiceImpl.unwrapCourse(courseRepository.findById(courseId), courseId);
        Student student = studentRepository.findById(studentId).get();
        Course course = courseRepository.findById(courseId).get();
        if(!student.getCourses().contains(course)) {
            throw new StudentNotEnrolledException(studentId, courseId);
        }
        grade.setStudent(student);
        grade.setCourse(course);
        return gradeRepository.save(grade);
        }
        throw new GradeNotFoundException(getErrorMessage(courseExists,studentExists));
    }

    
    @Override
    public Grade updateGrade(String score, Long studentId, Long courseId) {
        Optional<Grade> grade = gradeRepository.findByStudentIdAndCourseId(studentId, courseId);
        Grade unwrappedGrade = unwrapGrade(grade, studentId, courseId);
        unwrappedGrade.setScore(score);
        return gradeRepository.save(unwrappedGrade);
    }
    
    @Override
    public void deleteGrade(Long studentId, Long courseId) {
        if(!gradeRepository.existsByStudentIdAndCourseId(studentId, courseId)) 
        throw new ResourceNotFoundForDeletion();        
        gradeRepository.deleteByStudentIdAndCourseId(studentId, courseId);
    }

    @Override
    public List<Grade> getStudentGrades(Long studentId) {
        List<Grade> grades = gradeRepository.findByStudentId(studentId);
        if(grades.isEmpty()) throw new GradeNotFoundException("There are no grades for this student in our records");
        return grades;
    }
    
    @Override
    public List<Grade> getCourseGrades(Long courseId) {
        List<Grade> grades = gradeRepository.findByStudentId(courseId);
        if(grades.isEmpty()) throw new GradeNotFoundException("There are no grades for this course in our records");
        return grades;
    }
    
    @Override
    public List<Grade> getAllGrades() {
        List<Grade> grades = (List<Grade>) gradeRepository.findAll();
        if(grades.isEmpty()) throw new GradeNotFoundException("There are no grades in our records"); 
        return grades;
    }

    private Grade unwrapGrade(Optional<Grade> entity, Long studentId, Long courseId) {
        if (entity.isPresent()) return entity.get();
        else throw new GradeNotFoundException(studentId, courseId);
    }

    private String getErrorMessage(boolean courseExists, boolean studentExists) {
        if(!courseExists && !studentExists) return "Course ID and Student ID informed do not exist in our records";
        if(!courseExists) return "Course ID informed does not exist in our records";
        return "Student ID informed does not exist in our records";
    }

}
