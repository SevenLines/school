package ru.inrtu.backend.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "study_activity")
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudyActivity {

    /**
     * Свойства модели.
     */
    public static class Property {

        public static final String NAME = "name";

        public static final String DESCRIPTION = "description";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private Integer participationPoint;
    private Integer maxParticipants;
    //Enum ActivityType. Тип мероприятия: программа, курс, мероприятие
    private String activityType;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "activity_trajectory", joinColumns = {@JoinColumn(name = "study_activity_id")},
                inverseJoinColumns = {@JoinColumn(name = "trajectory_id")}
                )
    private Set<Trajectory> trajectories;


    @Override
    public String toString(){
        return "StudyActivity{ id= " + id +
                ", name= " + name +
                ", description= " + description +
                ", startDate= " + startDate +
                ", endDate= " + endDate +
                ", participationPoint= " + participationPoint +
                ", maxParticipants= " + maxParticipants +
                ", activityType= " + activityType +
                ", trajectories= " + trajectories +
                "}";
    }

    public void addTrajectory(Trajectory trajectory){
        trajectories.add(trajectory);
    }

}
