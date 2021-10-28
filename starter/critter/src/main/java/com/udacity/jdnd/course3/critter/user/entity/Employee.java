package com.udacity.jdnd.course3.critter.user.entity;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class Employee extends User {
    private EmployeeSkill skill;

    public EmployeeSkill getSkill() {
        return skill;
    }

    public void setSkill(EmployeeSkill skill) {
        this.skill = skill;
    }
}
