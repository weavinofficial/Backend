package Weavin.Entities;
import Weavin.Enums.Field;
import Weavin.Enums.ReportStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "FORUM_POSTS")
public class ForumPost {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    @Column
    private String title;

    @Column
    private String body;

    @Enumerated(EnumType.STRING)
    @Column
    private Field field;

    @Column
    private Date createdAt;

    @Column
    private Date updatedAt;

    @Column
    private boolean isUpdated;

    @Column
    private int likes;

    @Column
    private int views;

    @JsonIgnore
    @Column
    private int reports;

    @Column
    private boolean reportStatus = false;

    @OneToMany(mappedBy = "forumPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> commentList;

    @OneToMany(mappedBy = "forumPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tag> tagList;

}