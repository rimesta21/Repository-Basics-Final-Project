package com.udacity.jdnd.course3.critter.user.entity;

import com.udacity.jdnd.course3.critter.activity.entity.Activity;
import com.udacity.jdnd.course3.critter.pet.entity.Pet;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Nationalized
    private String name;
    private String email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Pet> pets;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Activity activity;


}
