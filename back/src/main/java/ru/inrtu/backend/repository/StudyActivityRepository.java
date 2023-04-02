package ru.inrtu.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.inrtu.backend.entity.StudyActivity;

public interface StudyActivityRepository extends JpaRepository<StudyActivity, Long> {

    @Query(nativeQuery = true,  value = "SELECT * FROM study_activity s WHERE" +
            " s.name LIKE :name AND s.start_date LIKE :startDate")
    StudyActivity findExistingInDB(String name, String startDate);
}
