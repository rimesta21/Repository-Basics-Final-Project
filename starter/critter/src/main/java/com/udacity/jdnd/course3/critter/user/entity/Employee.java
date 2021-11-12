package com.udacity.jdnd.course3.critter.user.entity;

import javax.persistence.Entity;
import java.util.Date;

/*
This represents an employee that has skills and availability
 */

@Entity
public class Employee extends User {
    String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
