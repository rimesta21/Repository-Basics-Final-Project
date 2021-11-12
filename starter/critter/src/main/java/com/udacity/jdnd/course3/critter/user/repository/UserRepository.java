package com.udacity.jdnd.course3.critter.user.repository;

import com.udacity.jdnd.course3.critter.user.entity.Customer;
import com.udacity.jdnd.course3.critter.user.entity.Employee;
import com.udacity.jdnd.course3.critter.user.entity.EmployeeSkill;
import com.udacity.jdnd.course3.critter.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT c FROM Customer c")
    List<Customer> getAllCustomers();

    @Query("SELECT DISTINCT e FROM Employee e JOIN EmployeeSkills es on es.employee.id = e.id JOIN EmployeeDayOfWeek ed on ed.employee.id = e.id WHERE ed.day = :day AND es.skill in :skills")
    List<Employee> getEmployeeBySkillAndAvailability(DayOfWeek day, Set<EmployeeSkill> skills);


    @Query("SELECT e FROM Employee e JOIN EmployeeDayOfWeek ed on ed.employee.id = e.id WHERE e.id = :id AND ed.day = :day")
    Employee employeeAvailableByIdAndDate(Long id, DayOfWeek day);

    @Query("SELECT e FROM Employee e JOIN EmployeeSkills es on es.employee.id = e.id WHERE e.id = :id AND es.skill = :skill")
    Employee employeeWithinSkillByIdAndSkill(Long id, EmployeeSkill skill);

    @Query("SELECT e FROM Employee e")
    List<Employee> getAllEmployees();

    boolean existsEmployeeById(Long id);

    boolean existsCustomerById(Long id);

}
