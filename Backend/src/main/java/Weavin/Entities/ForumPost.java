package Weavin.Entities;
import Weavin.Enums.Field;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Setter
@Getter
@Table(name = "FORUM_POSTS")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForumPost {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
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
    @CreationTimestamp
    private Date createdAt;

    @Column
    @UpdateTimestamp
    private Date updatedAt;

    @Column
    @Builder.Default()
    private boolean isUpdated = false;

    @Column
    @Builder.Default()
    private Integer likes = 0;

    @Column
    @Builder.Default()
    private Integer views = 0;

    @JsonIgnore
    @Column
    @Builder.Default()
    private Integer reports = 0;

    @Column
    @Builder.Default()
    private boolean reportStatus = false;

    @OneToMany(mappedBy = "forumPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> commentList;

    @OneToMany(mappedBy = "forumPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tag> tagList;

}