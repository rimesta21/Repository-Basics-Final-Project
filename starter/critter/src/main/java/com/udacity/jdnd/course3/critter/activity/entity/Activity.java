package com.udacity.jdnd.course3.critter.activity.entity;

import com.udacity.jdnd.course3.critter.schedule.entity.Schedule;
import com.udacity.jdnd.course3.critter.user.entity.User;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Activity {
    @Id
    @GeneratedValue
    private Long id;

    private String description;
    @Column(precision = 12, scale = 4)
    private BigDecimal price;
    private String notes;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "schedule_id", referencedColumnName = "id")
    private Schedule schedule;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private User employeeId;


}
