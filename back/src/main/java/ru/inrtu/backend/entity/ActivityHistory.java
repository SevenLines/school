package ru.inrtu.backend.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * Класс для таблицы в которой хранятся записи о всех когда-либо проводимых активностях.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Accessors(chain = true)
public class ActivityHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "schoolchild_id", nullable = false)
    private Schoolchild schoolchild;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "study_activity_id", nullable = false)
    private StudyActivity activity;
    private String recordCreateDate;
    //Enum ActivityHistoryRecordType
    private String historyRecordType;

    public ActivityHistory(Schoolchild schoolchild, StudyActivity activity,
                           String historyRecordType, String recordCreateDate) {
        this.schoolchild = schoolchild;
        this.activity = activity;
        this.historyRecordType = historyRecordType;
        this.recordCreateDate = recordCreateDate;
    }

    @Override
    public String toString() {
        return "ActivityHistory{" +
                "id=" + id +
                ", schoolchild=" + schoolchild +
                ", activity=" + activity +
                ", recordCreateDate='" + recordCreateDate + '\'' +
                ", historyRecordType='" + historyRecordType + '\'' +
                '}';
    }
}
