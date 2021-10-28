package com.udacity.jdnd.course3.critter.activity.repository;

import com.udacity.jdnd.course3.critter.activity.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
}
