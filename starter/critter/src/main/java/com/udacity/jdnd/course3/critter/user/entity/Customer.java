package com.udacity.jdnd.course3.critter.user.entity;


import javax.persistence.*;


@Entity
public class Customer extends User {
    private Long number_pets;

    public Long getNumber_pets() {
        return number_pets;
    }

    public void setNumber_pets(Long number_pets) {
        this.number_pets = number_pets;
    }
}
