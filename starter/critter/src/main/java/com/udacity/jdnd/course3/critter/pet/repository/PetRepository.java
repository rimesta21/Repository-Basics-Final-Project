package com.udacity.jdnd.course3.critter.pet.repository;

import com.udacity.jdnd.course3.critter.pet.entity.Pet;
import com.udacity.jdnd.course3.critter.user.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    @Query("SELECT p.owner FROM Pet p where p.id = :pedId")
    Customer getOwnerId(Long pedId);

    @Query("SELECT p FROM Pet p JOIN Customer c ON c.id = p.owner.id WHERE c.id = :id")
    List<Pet> getPetsByCustomerId(Long id);

}
