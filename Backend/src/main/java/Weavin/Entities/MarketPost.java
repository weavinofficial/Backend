package Weavin.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketPost {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", referencedColumnName = "id")
    private User user;

    @Column()
    private String productName;

    @Column()
    private String description;

    @Column()
    @CreationTimestamp
    private Date createdAt;

    @Column()
    @UpdateTimestamp
    private Date updatedAt;

    @Column()
    @Builder.Default()
    private boolean isUpdated = false;

    @Column()
    private String photo;

    @Column()
    @Builder.Default()
    private Integer price = 0;

    @JsonIgnore
    @Column()
    @Builder.Default()
    private Integer reports = 0;

    @Column()
    @Builder.Default()
    private boolean reportStatus = false;

    @Column()
    @Builder.Default()
    private Integer views = 0;

    @Column()
    @Builder.Default()
    private Integer likes = 0;

    @Column()
    @Builder.Default()
    private boolean sold = false;

    @Column()
    @Builder.Default()
    private Integer stock = 0;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "marketPost")
    @Builder.Default()
    private List<Comment> commentList = new ArrayList<Comment>();

}