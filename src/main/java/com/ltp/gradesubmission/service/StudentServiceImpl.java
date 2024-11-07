package com.ltp.gradesubmission.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ltp.gradesubmission.entity.Course;
import com.ltp.gradesubmission.entity.Student;
import com.ltp.gradesubmission.exception.CourseNotFoundException;
import com.ltp.gradesubmission.exception.GenericMessageException;
import com.ltp.gradesubmission.exception.ResourceNotFoundForDeletion;
import com.ltp.gradesubmission.exception.StudentNotFoundException;
import com.ltp.gradesubmission.repository.CourseRepository;
import com.ltp.gradesubmission.repository.StudentRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {

    // @Autowired
    StudentRepository studentRepository;
    CourseRepository courseRepository;

    @Override
    public Student getStudent(Long id) {
        Optional<Student> student = studentRepository.findById(id);
        return unwrapStudent(student, id);
    }

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudent(Long id) {
        if(!studentRepository.existsById(id)) throw new ResourceNotFoundForDeletion();
        if(!getStudent(id).getCourses().isEmpty()) {
            throw new GenericMessageException("Cannot delete student " + id + " because it is enrolled in courses", HttpStatus.BAD_REQUEST);
        }
        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> getStudents() {
        List<Student> students = (List<Student>) studentRepository.findAll();
        if(students.isEmpty()) throw new StudentNotFoundException("There are no students in our records");
        return students; 
    }
    
    @Override
    public Set<Course> getEnrolledCourses(Long id) {
        Student student = getStudent(id);
        Set<Course> courses = student.getCourses(); 
        if(courses.isEmpty()) throw new CourseNotFoundException("There are no courses for this student");
        return courses;
    }
    
    private Student unwrapStudent(Optional<Student> entity, Long id) {
        if (entity.isPresent()) return entity.get();
        else throw new StudentNotFoundException(id);
    }

}