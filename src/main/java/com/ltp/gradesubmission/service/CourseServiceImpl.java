package com.ltp.gradesubmission.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
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
public class CourseServiceImpl implements CourseService {

    CourseRepository courseRepository;
    StudentRepository studentRepository;

    @Override
    public Course getCourse(Long id) {
        Optional<Course> course = courseRepository.findById(id);
        return unwrapCourse(course, id);
    }

    @Override
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public void deleteCourse(Long id) {
        if(!courseRepository.existsById(id)) throw new ResourceNotFoundForDeletion();  
        if(!getCourse(id).getStudents().isEmpty()) {
            throw new GenericMessageException("Cannot delete course " + id + " because there are students enrolled", HttpStatus.BAD_REQUEST); 
        }
        courseRepository.deleteById(id);
    }

    @Override
    public List<Course> getCourses() {
        List<Course> courses = (List<Course>) courseRepository.findAll();
        if(courses.isEmpty()) throw new CourseNotFoundException("There are no courses in our records");
        return courses;
    }
    
    @Override
    public Course addStudentToCourse(Long id, Long studentId) {
        Course course = getCourse(id);
        Student student = studentRepository.findById(studentId).orElseThrow( () -> new StudentNotFoundException(studentId));
        if(course.getStudents().contains(student)) throw new DataIntegrityViolationException(null);
        // Student unwrappedStudent = StudentServiceImpl.unwrapStudent(student, studentId); // removido pois o metodo estático foi alterado para pertencer a instancias
        course.getStudents().add(student);
        return courseRepository.save(course);
    }

    public Set<Student> getEnrolledStudents(Long id) {
        Course course = getCourse(id);
        Set<Student> students = course.getStudents();
        if(students.isEmpty()) throw new StudentNotFoundException("There are no students enrolled in this course");
        return students;
    }

    @Override
    public void removeStudentFromCourse(Long id, Long studentId) {
        Course course = getCourse(id);
        Student student = studentRepository.findById(studentId).orElseThrow( () -> new StudentNotFoundException(studentId));
        Set<Student> students = course.getStudents();
        if(!students.contains(student)) throw new StudentNotFoundException("This student is not enrolled in this course");
        students.remove(student);
        courseRepository.save(course);
    }
    
    private Course unwrapCourse(Optional<Course> entity, Long id) {
        if (entity.isPresent()) return entity.get();
        throw new CourseNotFoundException(id);
    }

}
