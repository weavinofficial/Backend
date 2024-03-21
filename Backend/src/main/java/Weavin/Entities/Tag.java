package Weavin.Entities;

import Weavin.Enums.Field;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "TAGS")
public class Tag {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private ForumPost forumPost;

    @Enumerated(EnumType.STRING)
    @Column
    private Field field;

}
