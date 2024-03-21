package Weavin.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Comment{

    @Id
    @GeneratedValue
    private int id;

    @Column()
    private String body;

    @Column()
    private Date createAt;

    @Column()
    private Date updatedAt;

    @Column()
    private int likes;

    @JsonIgnore
    @Column()
    private int reports;

    @Column()
    private boolean reportStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", referencedColumnName = "id")
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marketPost", referencedColumnName = "id")
    private MarketPost marketPost;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forumPost", referencedColumnName = "id")
    private ForumPost forumPost;
}