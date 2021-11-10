package com.udacity.jdnd.course3.critter.pet.service;

import com.udacity.jdnd.course3.critter.pet.entity.Pet;
import com.udacity.jdnd.course3.critter.pet.repository.PetRepository;
import com.udacity.jdnd.course3.critter.schedule.entity.Schedule;
import com.udacity.jdnd.course3.critter.user.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public Customer getOwnerId(Long petId) {
        return petRepo.getOwnerId(petId);
    }

    public List<Schedule> getPetSchedules(Long id) {
        Pet pet = getPetById(id);
        if(pet != null) {
            return pet.getSchedules();
        }
        return null;
    }

    public boolean existsById(Long id) {
        return petRepo.existsById(id);
    }


}
