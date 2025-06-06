package com.example.supercourse.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column(length = 1024)
    private String description;
    @Column
    private int score = 0;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Photo> photos = new ArrayList<>();
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    @JsonIgnore
    private User user;

    public Course(Long id, String name, String desc, int score) {
        this.id = id;
        this.name = name;
        this.description = desc;
        this.score = score;
    }
    public void incrementScore() {
        System.out.println("score inc " + score);
        score++;
        System.out.println("Score inc after: " + score);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", score=" + score +
                ", photos=" + photos +
                ", user=" + user +
                '}';
    }
}
