package Weavin.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment{

    @Id
    @GeneratedValue
    private Integer id;

    @Column()
    private String body;

    @Column()
    @CreationTimestamp
    private Date createAt;

    @Column()
    @UpdateTimestamp
    private Date updatedAt;

    @Column()
    @Builder.Default()
    private Integer likes = 0;

    @JsonIgnore
    @Column()
    private Integer reports;

    @Column()
    @Builder.Default()
    private boolean reportStatus = false;

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