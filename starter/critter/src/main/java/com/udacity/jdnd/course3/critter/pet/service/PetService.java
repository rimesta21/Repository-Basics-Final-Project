package com.udacity.jdnd.course3.critter.pet.service;

import com.udacity.jdnd.course3.critter.pet.entity.Pet;
import com.udacity.jdnd.course3.critter.pet.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class PetService {
    @Autowired
    private PetRepository petRepo;

    public Pet getPetById(Long id) {
        Optional<Pet> petOptional = petRepo.findById(id);
        if(petOptional.isEmpty()) {
            return null;
        }
        return petOptional.get();
    }

    public Pet eatPet(Pet pet) {
        return petRepo.save(pet);
    }

}
