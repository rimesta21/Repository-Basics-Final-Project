package com.udacity.jdnd.course3.critter.activity.service;

import com.google.common.collect.Lists;
import com.udacity.jdnd.course3.critter.activity.entity.Activity;
import com.udacity.jdnd.course3.critter.activity.repository.ActivityRepository;
import com.udacity.jdnd.course3.critter.schedule.entity.Schedule;
import com.udacity.jdnd.course3.critter.user.entity.Employee;
import com.udacity.jdnd.course3.critter.user.entity.EmployeeDayOfWeek;
import com.udacity.jdnd.course3.critter.user.entity.EmployeeSkill;
import com.udacity.jdnd.course3.critter.user.entity.EmployeeSkills;
import com.udacity.jdnd.course3.critter.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepo;

    @Autowired
    private UserService userService;

    public Activity getActivityById(Long id) {
        return activityRepo.getById(id);
    }

    public void deleteActivityById(Long id) {activityRepo.deleteById(id);}


    public Activity saveEmployeesById(LocalDate date, List<Long> employeeIds, EmployeeSkill description) {
        int i = 0;
        int size = employeeIds.size();
        while(i < size) {
            Employee employee;
            Long id;
            try {
                id = employeeIds.get(i);
                employee = userService.findEmployeeById(id);
            } catch(IndexOutOfBoundsException e) {
                return null;
            }
            //filter out the ones that don't exist
            if(employee == null) {
                employeeIds.remove(i);
                size--;
                continue;
            }
            if(userService.availableDayForEmployee(id, date.getDayOfWeek())
                    && userService.withInSkillRangeForEmployee(id, description)) {
                Activity activity = new Activity(description, employee);
                if(employee.getActivities() != null) {
                    employee.getActivities().add(activity);
                } else {
                    employee.setActivities(Lists.newArrayList(activity));
                }
                //theoretically gives everyone a fair chance
                Collections.shuffle(employeeIds);
                return activity;
            }
            i++;
        }
        return null;
    }
}
