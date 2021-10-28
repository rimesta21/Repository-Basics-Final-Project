package com.udacity.jdnd.course3.critter.activity.service;

import com.udacity.jdnd.course3.critter.activity.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepo;
}
