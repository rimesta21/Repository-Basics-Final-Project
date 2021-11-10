package com.udacity.jdnd.course3.critter.activity.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.udacity.jdnd.course3.critter.schedule.controller.ScheduleViews;
import com.udacity.jdnd.course3.critter.schedule.entity.Schedule;
import com.udacity.jdnd.course3.critter.user.entity.Employee;
import com.udacity.jdnd.course3.critter.user.entity.EmployeeSkill;
import com.udacity.jdnd.course3.critter.user.entity.User;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Activity {
    @Id
    @GeneratedValue
    private Long id;
    @JsonView(ScheduleViews.Public.class)
    private EmployeeSkill description;
    @Column(precision = 12, scale = 4)
    @JsonView(ScheduleViews.Public.class)
    private BigDecimal price;
    private String notes;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "schedule_id", referencedColumnName = "id")
    private Schedule schedule;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    @JsonView(ScheduleViews.Public.class)
    private User employee;

    public Activity() {}

    public Activity(EmployeeSkill description, Employee employee) {
        this.description = description;
        this.employee = employee;
    }

    public Activity(EmployeeSkill description, Schedule schedule) {
        this.description = description;
        this.schedule = schedule;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmployeeSkill getDescription() {
        return description;
    }

    public void setDescription(EmployeeSkill description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }
}
