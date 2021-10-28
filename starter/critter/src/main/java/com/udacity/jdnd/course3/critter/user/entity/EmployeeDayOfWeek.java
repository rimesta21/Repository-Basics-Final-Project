package com.udacity.jdnd.course3.critter.user.entity;

import javax.persistence.*;
import java.time.DayOfWeek;

@Entity
public class EmployeeDayOfWeek {
    @Id
    @GeneratedValue
    private Long id;
    //This isn't necessary per se but JPA needs it

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private User employeeId;

    private DayOfWeek day;

}
