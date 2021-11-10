package com.udacity.jdnd.course3.critter.schedule.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Schedule already exists on this day.")
public class ScheduleAlreadyExists extends RuntimeException {
    public ScheduleAlreadyExists () {}

    public ScheduleAlreadyExists(String message) {
        super(message);
    }
}
