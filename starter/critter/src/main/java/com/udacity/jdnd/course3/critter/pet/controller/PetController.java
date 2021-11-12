package com.udacity.jdnd.course3.critter.pet.controller;

import com.udacity.jdnd.course3.critter.pet.entity.Pet;
import com.udacity.jdnd.course3.critter.pet.service.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    @Autowired
    PetService petService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = petService.eatPet(convertDTOToPet(petDTO), petDTO.getOwnerId());
        return convertEntityToPetDTO(pet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = petService.getPetById(petId);
        if(pet != null) {
            return convertEntityToPetDTO(petService.getPetById(petId));
        }
        return null;
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<Pet> pets = petService.listPets();
        if(pets != null) {
            return pets.stream().map(PetController::convertEntityToPetDTO).collect(Collectors.toList());
        }
        return null;
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> pets = petService.petsByOwner(ownerId);
        if(pets != null) {
            return pets.stream().map(PetController::convertEntityToPetDTO).collect(Collectors.toList());
        }
        return null;
    }

    private static PetDTO convertEntityToPetDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        petDTO.setBirthDate(pet.getBirthday());
        if(pet.getOwner() != null) {
            petDTO.setOwnerId(pet.getOwner().getId());
        }
        return petDTO;
    }

    private static Pet convertDTOToPet(PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        pet.setBirthday(petDTO.getBirthDate());
        return pet;
    }

}
