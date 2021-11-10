package com.udacity.jdnd.course3.critter.user.service;

import com.udacity.jdnd.course3.critter.pet.entity.Pet;
import com.udacity.jdnd.course3.critter.pet.service.PetService;
import com.udacity.jdnd.course3.critter.schedule.services.ScheduleService;
import com.udacity.jdnd.course3.critter.user.entity.*;
import com.udacity.jdnd.course3.critter.user.repository.EmployeeDayOfWeekRepository;
import com.udacity.jdnd.course3.critter.user.repository.EmployeeSkillsRepository;
import com.udacity.jdnd.course3.critter.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private EmployeeDayOfWeekRepository dayOfWeekRepo;
    @Autowired
    private EmployeeSkillsRepository skillsRepo;
    @Autowired
    private PetService petService;
    @Autowired
    private ScheduleService scheduleService;

    public Customer eatCustomer(Customer newCustomer, List<Long> petIds) {
        if(newCustomer.getId() != null) {
            Optional<User> optionalUser = userRepo.findById(newCustomer.getId());
            if(optionalUser.isPresent()) {
                Customer customer = (Customer) optionalUser.get();
                if(newCustomer.getName() != null) {
                    customer.setName(newCustomer.getName());
                }
                if(newCustomer.getNotes() != null) {
                    customer.setNotes(newCustomer.getNotes());
                }
                if(newCustomer.getPhoneNumber() != null) {
                    customer.setPhoneNumber(newCustomer.getPhoneNumber());
                }
                //only adding new pets, deleting pets would be another controller call
                if(petIds != null) {
                    List<Long> existingPetIds = customer.getPets().stream().map(Pet::getId).collect(Collectors.toList());
                    List<Pet> newPets = new ArrayList<>();
                    petIds.stream().filter(id -> petService.existsById(id) && !existingPetIds.contains(id))
                            .forEach(id -> {
                                Pet pet = petService.getPetById(id);
                                if(pet.getOwner() == null) {
                                    pet.setOwner(customer);
                                    newPets.add(pet);
                                }
                            });
                    customer.getPets().addAll(newPets);
                }
                return userRepo.save(customer);
            }
        }

        if(petIds != null) {
            List<Pet> pets = new ArrayList<>();
            petIds.stream()
                    .filter(id -> petService.getPetById(id) != null)
                    .forEach(id -> {
                        Pet pet = petService.getPetById(id);
                        if(pet.getOwner() == null) {
                            pet.setOwner(newCustomer);
                            pets.add(pet);
                        }
                    });
            newCustomer.setPets(pets);
        }
        return userRepo.save(newCustomer);
    }

    public User getUserByPetId(Long petId) {
        return petService.getOwnerId(petId);
    }

    public List<Customer> getAllCustomers() {
        return userRepo.getAllCustomers();
    }

    public User findUserById(Long userId) {
        return userRepo.getById(userId);
    }

    public User eatEmployee(Employee newEmployee, Set<EmployeeSkill> skills, Set<DayOfWeek> availability) {
        if(newEmployee.getId() != null) {
            Optional<User> userOptional = userRepo.findById(newEmployee.getId());
            if(userOptional.isPresent()) {
                Employee employee = (Employee) userOptional.get();
                if(newEmployee.getName() != null) {
                    employee.setName(newEmployee.getName());
                }
                if(newEmployee.getEmail() != null) {
                    employee.setEmail(newEmployee.getEmail());
                }
                //only adding new skills, deleting skills would be another controller call
                if (skills != null) {
                    Set<EmployeeSkill> existingSkills = employee.getSkills().stream().map(EmployeeSkills::getSkill).collect(Collectors.toSet());
                    Set<EmployeeSkills> newSkills = new HashSet<>();
                    skills.stream().filter(skill -> !existingSkills.contains(skill))
                            .forEach(skill -> {
                                EmployeeSkills es = new EmployeeSkills(skill);
                                es.setEmployee(employee);
                                newSkills.add(es);
                            });
                    employee.getSkills().addAll(newSkills);
                }

                if(availability != null) {
                    Set<DayOfWeek> existingAvailability = employee.getDaysAvailable().stream().map(EmployeeDayOfWeek::getDay).collect(Collectors.toSet());
                    Set<EmployeeDayOfWeek> newDays = new HashSet<>();

                    availability.stream().filter(day -> !existingAvailability.contains(day))
                            .forEach(day -> {
                                EmployeeDayOfWeek newDay = new EmployeeDayOfWeek(day);
                                newDay.setEmployee(employee);
                                newDays.add(newDay);
                            });
                    employee.getDaysAvailable().addAll(newDays);
                }
                return userRepo.save(employee);
            }
        }

        if(skills != null) {
            Set<EmployeeSkills> employeeSkills = new HashSet<>();
            skills.forEach(skill -> {
                EmployeeSkills es = new EmployeeSkills(skill);
                es.setEmployee(newEmployee);
                employeeSkills.add(es);
            });
            newEmployee.setSkills(employeeSkills);
        }
        if(availability != null) {
            saveDaysOfWeek(availability, newEmployee);
        }
        return userRepo.save(newEmployee);
    }

    public Employee findEmployeeById(Long id) {
        Optional<User> oppUser =  userRepo.findById(id);
        if(oppUser.isEmpty()) {
            return null;
        }
        return (Employee) oppUser.get();
    }

    private void saveDaysOfWeek(Set<DayOfWeek> daysAvailable, Employee employee) {
        Set<EmployeeDayOfWeek> allDays = new HashSet<>();
        daysAvailable.forEach(availableDay -> {
            EmployeeDayOfWeek day = new EmployeeDayOfWeek(availableDay);
            day.setEmployee(employee);
            allDays.add(day);
        });
        employee.setDaysAvailable(allDays);
    }

    public void setEmployeeAvailability(Set<DayOfWeek> daysAvailable, Long employeeId) {
        Optional<User> oppEmployee = userRepo.findById(employeeId);
        if(oppEmployee.isPresent()) {
            Employee employee = (Employee) oppEmployee.get();
            saveDaysOfWeek(daysAvailable, employee);
            userRepo.save(employee);
        }
    }

    public List<Employee> findEmployeesByDateAndSkill(LocalDate date, Set<EmployeeSkill> skills) {
        return userRepo.getEmployeeBySkillAndAvailability(date.getDayOfWeek(), skills)
                .stream().filter(e -> scheduleService.checkIfEmployeeAlreadyBooked(date, e)).collect(Collectors.toList());
    }

    public boolean availableDayForEmployee(Long id, DayOfWeek day) {
        return userRepo.employeeAvailableByIdAndDate(id, day) != null;
    }

    public boolean withInSkillRangeForEmployee(Long id, EmployeeSkill skill) {
        return userRepo.employeeWithinSkillByIdAndSkill(id, skill) != null;
    }

    public List<Employee> getAllEmployees() {
        return userRepo.getAllEmployees();
    }

    public boolean employeeExists(Long id) {
        return userRepo.existsEmployeeById(id);
    }



}
