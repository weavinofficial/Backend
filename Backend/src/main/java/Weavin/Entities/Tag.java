package Weavin.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import Weavin.Enums.Field;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "TAGS")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tag {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private ForumPost forumPost;

    @Enumerated(EnumType.STRING)
    @Column
    private Field field;

}
