package Weavin.Entities;

import Weavin.Enums.Field;
import Weavin.Enums.Presence;
import Weavin.Enums.ReportStatus;
import Weavin.Enums.Role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "USERS")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    public User(String username, String email, Role role) {
        this.username = username;
        this.email = email;
        this.role = role;
    }

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String username;

    @JsonIgnore
    @Column
    private boolean usernameAlreadyChanged;

    @JsonIgnore
    @Column
    private String email;

    @JsonIgnore
    @Column
    private String password;

    @Column
    private Date lastSeenAt;

    @Column
    private String profilePhoto;

    @Enumerated(EnumType.STRING)
    @Column
    private Presence presence;

    @Enumerated(EnumType.STRING)
    @Column
    private Field field;

    @JsonIgnore
    @Column
    private int reports;

    @Enumerated(EnumType.STRING)
    @Column
    @Builder.Default()
    private ReportStatus reportStatus = ReportStatus.SAFE;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<MarketPost> marketPostList;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Comment> commentList;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Semester> semesterList;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<ForumPost> forumPostList;

    @Column
    private Role role;

    @Column
    private String refreshToken;

    
}