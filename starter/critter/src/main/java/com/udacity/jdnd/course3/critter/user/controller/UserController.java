package com.udacity.jdnd.course3.critter.user.controller;

import com.udacity.jdnd.course3.critter.pet.entity.Pet;
import com.udacity.jdnd.course3.critter.pet.service.PetService;
import com.udacity.jdnd.course3.critter.user.controller.CustomerDTO;
import com.udacity.jdnd.course3.critter.user.controller.EmployeeDTO;
import com.udacity.jdnd.course3.critter.user.controller.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.user.entity.*;
import com.udacity.jdnd.course3.critter.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        User customer = userService.eatCustomer(convertDTOToCustomer(customerDTO), customerDTO.getPetIds());
        return convertEntityToCustomerDTO(customer);
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customers = userService.getAllCustomers();
        if(customers != null) {
            return customers.stream()
                    .map(UserController::convertEntityToCustomerDTO)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        return convertEntityToCustomerDTO(userService.getUserByPetId(petId));
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = convertDTOToEmployee(employeeDTO);
        return convertEntityToEmployeeDTO(userService.eatEmployee(employee, employeeDTO.getSkills(), employeeDTO.getDaysAvailable()));
    }

    @GetMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        return convertEntityToEmployeeDTO(userService.findUserById(employeeId));
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        userService.setEmployeeAvailability(daysAvailable, employeeId);
    }

    @PutMapping("/employee/skills/{employeeId}")
    public void setSkills(@RequestBody Set<EmployeeSkill> skills, @PathVariable Long employeeId) {
        userService.setEmployeeSkills(skills, employeeId);
    }

    @PostMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employees = userService.findEmployeesByDateAndSkill(employeeDTO.getDate(), employeeDTO.getSkills());
        if(employees != null) {
            return employees.stream().map(UserController::convertEntityToEmployeeDTO).collect(Collectors.toList());
        }
        return null;
    }

    @GetMapping("/employees")
    public List<EmployeeDTO> listEmployees() {
        List<Employee> employees = userService.getAllEmployees();
        if(employees != null) {
            return employees.stream().map(UserController::convertEntityToEmployeeDTO).collect(Collectors.toList());
        }
        return null;
    }

    private static CustomerDTO convertEntityToCustomerDTO(User user) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(user, customerDTO);
        List<Pet> pets = user.getPets();
        if(pets != null) {
            customerDTO.setPetIds(pets.stream().map(Pet::getId).collect(Collectors.toList()));
        }
        return customerDTO;
    }

    private static Customer convertDTOToCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

    private static EmployeeDTO convertEntityToEmployeeDTO(User user) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(user, employeeDTO);
        Set<EmployeeSkills> skills = user.getSkills();
        if(skills != null) {
            employeeDTO.setSkills(skills.stream().map(EmployeeSkills::getSkill).collect(Collectors.toSet()));
        }
        Set<EmployeeDayOfWeek> availability = user.getDaysAvailable();
        if(availability != null) {
            employeeDTO.setDaysAvailable(availability.stream().map(EmployeeDayOfWeek::getDay).collect(Collectors.toSet()));
        }
        return employeeDTO;
    }

    private static Employee convertDTOToEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return employee;
    }

}
