package com.udacity.jdnd.course3.critter.schedule.services;

import com.google.common.collect.Lists;
import com.udacity.jdnd.course3.critter.activity.entity.Activity;
import com.udacity.jdnd.course3.critter.activity.service.ActivityService;
import com.udacity.jdnd.course3.critter.pet.entity.Pet;
import com.udacity.jdnd.course3.critter.pet.service.PetService;
import com.udacity.jdnd.course3.critter.schedule.entity.Schedule;
import com.udacity.jdnd.course3.critter.schedule.repository.ScheduleRepository;
import com.udacity.jdnd.course3.critter.user.entity.Employee;
import com.udacity.jdnd.course3.critter.user.entity.EmployeeSkill;
import com.udacity.jdnd.course3.critter.user.entity.EmployeeSkills;
import com.udacity.jdnd.course3.critter.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepo;
    @Autowired
    private PetService petService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private UserService userService;

    public void deleteScheduleById(Long id) {
        scheduleRepo.deleteById(id);
    }

    @Transactional
    public Schedule eatSchedule(Schedule schedule, List<Long> employeeIds, List<Long> petIds, Set<EmployeeSkill> activities)  {
        if(schedule.getId() != null) {
            Optional<Schedule> scheduleOptional = scheduleRepo.findById(schedule.getId());
            if (scheduleOptional.isPresent()) {
                Schedule scheduleUpdate = scheduleOptional.get();

                if (scheduleUpdate.getDate() != schedule.getDate() && !scheduleRepo.existsScheduleByDate(schedule.getDate())) {
                    scheduleUpdate.setDate(schedule.getDate());
                    List<Activity> deleted = new ArrayList<>();
                    List<Activity> copy = new ArrayList<>(scheduleUpdate.getActivities());
                    for(Activity activity : copy) {
                        if(!userService.availableDayForEmployee(activity.getEmployee().getId(), scheduleUpdate.getDate().getDayOfWeek())
                            && !deleted.contains(activity)) {
                            deleted.add(activity);
                            scheduleUpdate.getActivities().remove(activity);
                            activityService.deleteActivityById(activity.getId());
                        }
                    }
                    scheduleRepo.save(scheduleUpdate);
                }

                List<Long> newEmployeeIds = updateEmployeeList(scheduleUpdate.getActivities(), employeeIds);
                if (addPets(scheduleUpdate, petIds) && activities != null && employeeIds != null) {
                    activities.forEach(description -> {
                        Activity activity = activityService.saveEmployeesById(scheduleUpdate.getDate(), newEmployeeIds, description);
                        if(activity != null) {
                            activity.setSchedule(scheduleUpdate);
                            scheduleUpdate.getActivities().add(activity);
                        }
                    });
                    return scheduleRepo.save(scheduleUpdate);
                } else if (activities != null && employeeIds != null) {
                    Set<EmployeeSkill> existingActivities = scheduleUpdate.getActivities().stream().map(Activity::getDescription)
                            .collect(Collectors.toSet());
                    activities.stream().filter(description -> !existingActivities.contains(description))
                            .forEach(description -> {
                                Activity activity = activityService.saveEmployeesById(scheduleUpdate.getDate(), newEmployeeIds, description);
                                if(activity != null) {
                                    activity.setSchedule(scheduleUpdate);
                                    scheduleUpdate.getActivities().add(activity);
                                }
                            });
                /*
                This assumes that if only the employeeIds are submitted, then and add new the new employees and add new
                Activities from the new employs' skill set
                 */
                } else if (employeeIds != null) {
                    Set<EmployeeSkill> existingActivities = scheduleUpdate.getActivities().stream().map(Activity::getDescription)
                            .collect(Collectors.toSet());
                    createActivitiesFromEmployees(existingActivities, newEmployeeIds, scheduleUpdate);
                }
                return scheduleRepo.save(scheduleUpdate);
            }
        }

        if(schedule.getDate() != null && scheduleRepo.findScheduleByDate(schedule.getDate()) == null) {
            savePets(schedule, petIds);
            schedule.setActivities(new ArrayList<>());
            if(activities != null && employeeIds != null) {
                activities.forEach(description -> {
                    Activity activity = activityService.saveEmployeesById(schedule.getDate(), employeeIds, description);
                    if(activity != null) {
                        schedule.getActivities().add(activity);
                        activity.setSchedule(schedule);
                    }
                });
            } else if (employeeIds != null) {
                Set<EmployeeSkill> addedActivities = new HashSet<>();
                createActivitiesFromEmployees(addedActivities, employeeIds, schedule);
            }
            return scheduleRepo.save(schedule);
        }
        throw new ScheduleAlreadyExists("Schedule already exists on this date.");
    }

    private void createActivitiesFromEmployees(Set<EmployeeSkill> existingActivities, List<Long> employeeIds, Schedule schedule) {
        employeeIds.stream().filter(id -> userService.employeeExists(id)
                                        && userService.availableDayForEmployee(id, schedule.getDate().getDayOfWeek())
                                        && checkIfEmployeeAlreadyBooked(schedule.getDate(), userService.findEmployeeById(id)))
                .forEach(id -> {
                    Employee employee = userService.findEmployeeById(id);
                    for(EmployeeSkills skills : employee.getSkills()) {
                        EmployeeSkill skill = skills.getSkill();
                        if(!existingActivities.contains(skill)) {
                            Activity activity = new Activity(skill, schedule);
                            activity.setEmployee(employee);
                            schedule.getActivities().add(activity);
                            existingActivities.add(skill);
                            break;
                        }
                    }
                });
    }

    private List<Long> updateEmployeeList(List<Activity> activities, List<Long> newEmployeeIds) {
        if(activities != null) {
            List<Long> employeeIds = new ArrayList<>();
            activities.forEach(activity -> {
                employeeIds.add(activity.getEmployee().getId());
            });
            newEmployeeIds.stream().filter(id -> !employeeIds.contains(id)).forEach(employeeIds::add);
            return employeeIds;
        }
        return newEmployeeIds;
    }

    public List<Schedule> listSchedules() {
        return scheduleRepo.findAll();
    }

    public boolean checkIfEmployeeAlreadyBooked(LocalDate date, Employee employee) {
        Schedule schedule = scheduleRepo.findScheduleByDate(date);
        if(schedule != null) {
            List<Activity> activities = schedule.getActivities();
            for (Activity activity : activities) {
                if (Objects.equals(activity.getEmployee(), employee)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean petExistsAndCanBeScheduled(Schedule schedule, Long petId) {
        Pet pet = petService.getPetById(petId);
        return pet != null && !schedule.getPets().contains(pet);
    }

    private boolean addPets(Schedule schedule, List<Long> petIds) {
        if(petIds != null) {
            AtomicBoolean added = new AtomicBoolean(false);
            petIds.stream()
                    .filter(id -> petExistsAndCanBeScheduled(schedule, id))
                    .forEach(id -> {
                        Pet pet = petService.getPetById(id);
                        pet.getSchedules().add(schedule);
                        schedule.getPets().add(pet);
                        added.set(true);
                    });
            return added.get();
        }
        return false;
    }

    private void savePets(Schedule schedule, List<Long> petIds) {
        if(petIds != null) {
            List<Pet> pets = new ArrayList<>();
            petIds.stream()
                    .filter(id -> petService.getPetById(id) != null)
                    .forEach(id -> {
                        Pet pet = petService.getPetById(id);
                        if(pet.getSchedules() != null) {
                            pet.getSchedules().add(schedule);
                        } else {
                            pet.setSchedules(Lists.newArrayList(schedule));
                        }
                        pets.add(pet);
                    });
            schedule.setPets(pets);
        }
    }

    public List<Schedule> getSchedulesByEmployeeId(Long id) {
        return scheduleRepo.getScheduleFromEmployeeId(id);
    }
}
