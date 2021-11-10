package com.udacity.jdnd.course3.critter.schedule.repository;

import com.udacity.jdnd.course3.critter.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Schedule findScheduleByDate(LocalDate date);

    boolean existsScheduleById(Long id);

    boolean existsScheduleByDate(LocalDate date);

    void deleteById(Long id);
}
