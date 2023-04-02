package ru.inrtu.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.inrtu.backend.dto.ActivityHistorySchoolchildrenInterface;
import ru.inrtu.backend.entity.Schoolchild;

import java.util.List;

public interface SchoolchildRepository extends JpaRepository<Schoolchild, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM schoolchild s WHERE s.name LIKE :name " +
            "AND s.surname LIKE :surname " +
            "AND s.father_name LIKE :fatherName " +
            "AND educational_class LIKE :educationalClass")
    Schoolchild findExistingInDB(String name, String surname, String fatherName, String educationalClass);

    @Query(nativeQuery = true, value = "SELECT activity_history.history_record_type, activity_history.record_create_date, " +
            "activity_history.study_activity_id, activity_history.schoolchild_id, " +
            "schoolchild.id, " +
            "schoolchild.educational_class AS educationalClass, " +
            "schoolchild.educational_organization AS educationalOrganization, " +
            "schoolchild.email, " +
            "schoolchild.father_name AS fatherName, " +
            "schoolchild.name, " +
            "schoolchild.phone_number AS phoneNumber, " +
            "schoolchild.surname AS surname " +
            "FROM activity_history " +
            "JOIN schoolchild ON activity_history.schoolchild_id=schoolchild.id " +
            "WHERE activity_history.study_activity_id=:activityId " +
            "AND activity_history.history_record_type='registered'")
    List<ActivityHistorySchoolchildrenInterface>findRegisteredSchoolchildren(Long activityId);

    @Query(nativeQuery = true, value = "SELECT activity_history.history_record_type, activity_history.record_create_date, " +
            "activity_history.study_activity_id, activity_history.schoolchild_id, " +
            "schoolchild.id, " +
            "schoolchild.educational_class AS educationalClass, " +
            "schoolchild.educational_organization AS educationalOrganization, " +
            "schoolchild.email, " +
            "schoolchild.father_name AS fatherName, " +
            "schoolchild.name, " +
            "schoolchild.phone_number AS phoneNumber, " +
            "schoolchild.surname AS surname " +
            "FROM activity_history " +
            "JOIN schoolchild ON activity_history.schoolchild_id=schoolchild.id " +
            "WHERE activity_history.study_activity_id=:activityId " +
            "AND activity_history.history_record_type='attended'")
    List<ActivityHistorySchoolchildrenInterface>findAttendedSchoolchildren(Long activityId);


    Schoolchild findByEmail(String email);
}
