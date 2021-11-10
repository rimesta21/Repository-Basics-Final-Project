package com.udacity.jdnd.course3.critter.schedule.repository;

import com.udacity.jdnd.course3.critter.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Schedule findScheduleByDate(LocalDate date);

    boolean existsScheduleById(Long id);

    boolean existsScheduleByDate(LocalDate date);

    void deleteById(Long id);

    @Query("SELECT DISTINCT s FROM Schedule s JOIN Activity a ON a.schedule.id = s.id JOIN Employee e ON e.id = a.employee.id WHERE e.id = :id")
    List<Schedule> getScheduleFromEmployeeId(Long id);
}
