package com.ltp.gradesubmission.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.ManyToAny;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.NonNull;

@Entity
@Table(name = "student", uniqueConstraints = {
    @UniqueConstraint(columnNames={"student_name", "student_birth_date"})
})
@Getter
@Setter
// @AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;

    @NonNull
    @NotBlank(message = "Name cannot be blank")
    @Column(name = "student_name", nullable = false)
    private String name;

    @NonNull
    @Past(message = "The birth date must be in the past")
    @Column(name = "student_birth_date", nullable = false)
    private LocalDate birthDate;

    @JsonIgnore
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Grade> grades;

    @JsonIgnore // evitar loop infinito.
    @ManyToMany(mappedBy = "students") // mappedBy concede a propriedade da tabela com join à outra entidade, o que impede que um estudante seja removido caso esteja matriculado em algum curso.
    // @ManyToMany
    // @JoinTable(name = "course_student",
    //     joinColumns = @JoinColumn(
    //         name = "student_id",
    //         referencedColumnName = "student_id"
    //     ),
    //     inverseJoinColumns = @JoinColumn(
    //         name = "course_id",
    //         referencedColumnName = "course_id"
    //     )
    // )
    private Set<Course> courses;

}
