package com.udacity.jdnd.course3.critter.user.entity;

import javax.persistence.*;

@Entity
public class EmployeeSkills {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private User employee;

    private EmployeeSkill skill;

    public EmployeeSkills() {}

    public EmployeeSkills(EmployeeSkill skill) {
        this.skill = skill;
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

    public void setEmployeeId(User employee) {
        this.employee = employee;
    }

    public EmployeeSkill getSkill() {
        return skill;
    }

    public void setSkill(EmployeeSkill skill) {
        this.skill = skill;
    }
}
