package com.udacity.jdnd.course3.critter.schedule.entity;

import com.udacity.jdnd.course3.critter.activity.entity.Activity;
import com.udacity.jdnd.course3.critter.pet.entity.Pet;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Schedule {
    @Id
    @GeneratedValue
    private Long id;

    private Date day;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "schedules", cascade = CascadeType.ALL)
    @JoinTable(
            name = "Schedule_pets",
            joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "pet_id"))
    private List<Pet> pets;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<Activity> activities;
}
