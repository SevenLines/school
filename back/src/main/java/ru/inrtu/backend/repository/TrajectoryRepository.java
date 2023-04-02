package ru.inrtu.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.inrtu.backend.entity.Trajectory;

public interface TrajectoryRepository extends JpaRepository<Trajectory, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM trajectory t WHERE" +
            " t.description LIKE :description AND" +
            " t.name LIKE :name")
    Trajectory findExistingInDB(String description, String name);

}
