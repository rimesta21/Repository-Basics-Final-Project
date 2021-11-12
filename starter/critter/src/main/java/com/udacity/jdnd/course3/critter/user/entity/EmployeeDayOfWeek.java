package com.udacity.jdnd.course3.critter.user.entity;

import javax.persistence.*;
import java.time.DayOfWeek;

@Entity
public class EmployeeDayOfWeek {
    @Id
    @GeneratedValue
    private Long id;
    //This isn't necessary per se but JPA needs it

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false, updatable = false)
    private User employee;

    private DayOfWeek day;

    public EmployeeDayOfWeek() {}

    public EmployeeDayOfWeek(DayOfWeek day) {
        this.day = day;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }
}
