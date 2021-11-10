package com.udacity.jdnd.course3.critter.schedule.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.udacity.jdnd.course3.critter.activity.entity.Activity;
import com.udacity.jdnd.course3.critter.pet.entity.Pet;
import com.udacity.jdnd.course3.critter.pet.service.PetService;
import com.udacity.jdnd.course3.critter.schedule.entity.Schedule;
import com.udacity.jdnd.course3.critter.schedule.services.ScheduleService;
import com.udacity.jdnd.course3.critter.user.entity.Employee;
import com.udacity.jdnd.course3.critter.user.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private PetService petService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return convertScheduleToDTO(scheduleService.eatSchedule(convertDTOToSchedule(scheduleDTO),
                scheduleDTO.getEmployeeIds(), scheduleDTO.getPetIds(), scheduleDTO.getActivities()));
    }

    @JsonView(ScheduleViews.Public.class)
    @PostMapping("/testing")
    public Schedule TestCreateSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return scheduleService.eatSchedule(convertDTOToSchedule(scheduleDTO),
                scheduleDTO.getEmployeeIds(), scheduleDTO.getPetIds(), scheduleDTO.getActivities());
    }

    @GetMapping("/{id}")
    public void deleteSchedule(@PathVariable long id) {
        scheduleService.deleteScheduleById(id);
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        return scheduleService.listSchedules().stream().map(ScheduleController::convertScheduleToDTO).collect(Collectors.toList());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> schedules = petService.getPetSchedules(petId);
        if(schedules != null) {
            return schedules.stream().map(ScheduleController::convertScheduleToDTO).collect(Collectors.toList());
        }
        return null;
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        throw new UnsupportedOperationException();
    }

    private static Schedule convertDTOToSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO,schedule);
        return schedule;
    }

    private static ScheduleDTO convertScheduleToDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);
        List<Activity> activities = schedule.getActivities();
        if(activities != null) {
            List<Long> employeeIds = new ArrayList<>();
            activities.forEach(activity -> {
                employeeIds.add(activity.getEmployee().getId());
            });
            scheduleDTO.setEmployeeIds(employeeIds.stream().distinct().collect(Collectors.toList()));
            scheduleDTO.setActivities(activities.stream().map(Activity::getDescription).collect(Collectors.toSet()));
        }
        List<Pet> pets = schedule.getPets();
        if(pets != null) {
            scheduleDTO.setPetIds(pets.stream().map(Pet::getId).collect(Collectors.toList()));
        }
        return scheduleDTO;
    }
}
