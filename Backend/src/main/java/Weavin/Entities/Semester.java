package Weavin.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "SEMESTERS")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Semester {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private int numOfCourse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    @Column
    private double preSuGpa;

    @Column
    private double postSuGpa;

    @OneToMany(mappedBy = "semester", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Course> courseList;

}
