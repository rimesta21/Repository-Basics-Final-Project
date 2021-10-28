package com.udacity.jdnd.course3.critter.user.entity;


import javax.persistence.*;


@Entity
public class Customer extends User {
    @Column(name = "phone_number")
    private String phoneNumber;
    private String notes;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
