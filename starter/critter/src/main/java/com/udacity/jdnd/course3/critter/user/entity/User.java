package com.udacity.jdnd.course3.critter.user.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.udacity.jdnd.course3.critter.activity.entity.Activity;
import com.udacity.jdnd.course3.critter.pet.entity.Pet;
import com.udacity.jdnd.course3.critter.schedule.controller.ScheduleViews;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/*
Abstract class of a user that is either a Customer or a employee. This is where all the table mappings are.
 */


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Nationalized
    @JsonView(ScheduleViews.Public.class)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Pet> pets;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee", cascade = CascadeType.MERGE)
    private List<Activity> activities;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "employee", cascade = CascadeType.ALL)
    private Set<EmployeeSkills> skills;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "employee", cascade = CascadeType.ALL)
    private Set<EmployeeDayOfWeek> daysAvailable;

    public User(){}

    public User(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Set<EmployeeSkills> getSkills() {
        return skills;
    }

    public void setSkills(Set<EmployeeSkills> skills) {
        this.skills = skills;
    }

    public Set<EmployeeDayOfWeek> getDaysAvailable() {
        return daysAvailable;
    }

    public void setDaysAvailable(Set<EmployeeDayOfWeek> daysAvailable) {
        this.daysAvailable = daysAvailable;
    }


}
