package edu.deanza.calendar.models;

import java.util.List;

public class IccClub extends Organization {

    private List<Day> meetingDays;

    public IccClub() {}

    public IccClub(String name, String description, String location, String facebookUrl,
                   List<Day> meetingDays) {
        super(name, description, location, facebookUrl);
        this.meetingDays = meetingDays;
    }

    public List<Day> getMeetingDays() {
        return meetingDays;
    }

}
