package com.udacity.jdnd.course3.critter.pet.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.udacity.jdnd.course3.critter.schedule.controller.ScheduleViews;
import com.udacity.jdnd.course3.critter.schedule.entity.Schedule;
import com.udacity.jdnd.course3.critter.user.entity.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Pet {
    @Id
    @GeneratedValue
    private Long id;
    @JsonView(ScheduleViews.Public.class)
    private String name;
    @JsonView(ScheduleViews.Public.class)
    private PetType type;
    private String notes;
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @JsonView(ScheduleViews.Public.class)
    private User owner;

    @ManyToMany(mappedBy = "pets", cascade = CascadeType.ALL)
    private List<Schedule> schedules;

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

    public PetType getType() {
        return type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getBirthday() {
        return birthDate;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthDate = birthday;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }
}
