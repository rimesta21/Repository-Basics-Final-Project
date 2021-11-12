package com.udacity.jdnd.course3.critter.pet.service;

import com.udacity.jdnd.course3.critter.pet.entity.Pet;
import com.udacity.jdnd.course3.critter.pet.repository.PetRepository;
import com.udacity.jdnd.course3.critter.schedule.entity.Schedule;
import com.udacity.jdnd.course3.critter.user.entity.Customer;
import com.udacity.jdnd.course3.critter.user.service.UserService;
import com.udacity.jdnd.course3.critter.utility.NullAwareBeanUtilsBean;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;


@Service
public class PetService {
    @Autowired
    private PetRepository petRepo;

    @Autowired
    private UserService userService;

    public Pet getPetById(Long id) {
        Optional<Pet> petOptional = petRepo.findById(id);
        if(petOptional.isEmpty()) {
            return null;
        }
        return petOptional.get();
    }

    public Pet eatPet(Pet newPet, Long ownerId) {
        if(newPet.getId() != null) {
            Optional<Pet> optionalPet = petRepo.findById(newPet.getId());
            //if the pet does exist then just update it instead
            if(optionalPet.isPresent()) {
                Pet pet = optionalPet.get();
                //copy all non-null entries
                try {
                    BeanUtilsBean notNull = new NullAwareBeanUtilsBean();
                    notNull.copyProperties(pet, newPet);
                } catch(IllegalAccessException | InvocationTargetException e) {
                    e.getCause();
                }

                if(ownerId != null && userService.employeeExists(ownerId)) {
                    //update the owner
                    Customer oldCustomer = pet.getOwner();
                    if(oldCustomer != null && oldCustomer.getPets() != null) {
                        oldCustomer.getPets().remove(pet);
                    }
                    Customer newCustomer = userService.findCustomerById(ownerId);
                    pet.setOwner(newCustomer);
                    newCustomer.getPets().add(pet);
                }
                return petRepo.save(pet);
            }
        }
        //I get a lot of errors without this
        newPet.setId(null);
        if(ownerId != null && userService.employeeExists(ownerId)) {
            Customer customer = userService.findCustomerById(ownerId);
            //set the customer
            if(customer.getPets() != null) {
                customer.getPets().add(newPet);
            } else {
                customer.setPets(Lists.newArrayList(newPet));
            }
            newPet.setOwner(customer);
        }
        return petRepo.save(newPet);
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

    public List<Pet> getPetsByCustomerId(Long id) {
        return petRepo.getPetsByCustomerId(id);
    }

    public List<Pet> listPets() {
        return petRepo.findAll();
    }

    public List<Pet> petsByOwner(Long ownerId) {
        return userService.getCustomerPets(ownerId);
    }

}
