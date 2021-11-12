package com.udacity.jdnd.course3.critter.schedule.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.udacity.jdnd.course3.critter.activity.entity.Activity;
import com.udacity.jdnd.course3.critter.pet.entity.Pet;
import com.udacity.jdnd.course3.critter.schedule.controller.ScheduleViews;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/*
This represents a schedule that is on a day and has pets and activities.
 */


@Entity
public class Schedule {
    @Id
    @GeneratedValue
    @JsonView(ScheduleViews.Public.class)
    private Long id;
    @JsonView(ScheduleViews.Public.class)
    private LocalDate date;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "Schedule_pets",
            joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "pet_id"))
    @JsonView(ScheduleViews.Public.class)
    private List<Pet> pets;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "schedule", cascade = CascadeType.MERGE)
    @JsonView(ScheduleViews.Public.class)
    private List<Activity> activities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }
}
