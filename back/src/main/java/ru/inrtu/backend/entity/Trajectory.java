package ru.inrtu.backend.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Trajectory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String description;


    public Trajectory(String name, String description, Set<StudyActivity> activities) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Trajectory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
