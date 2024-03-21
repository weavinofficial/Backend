package Weavin.Entities;

import Weavin.Enums.Grade;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "COURSES")
public class Course {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Semester semester;

    @Column
    private String courseCode;

    @Column
    private String courseName;

    @Column
    private Grade grade;

}
