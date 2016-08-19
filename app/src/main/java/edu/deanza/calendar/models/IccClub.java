package edu.deanza.calendar.models;

import org.joda.time.Interval;

import java.util.List;

public class IccClub extends Organization {

    private final List<Day> meetingDays;

    public IccClub(String name, String description, String location, String facebookUrl,
                   List<Interval> meetings, List<Day> meetingDays) {
        super(name, description, location, facebookUrl, meetings);
        this.meetingDays = meetingDays;
    }

    public List<Day> getMeetingDays() {
        return meetingDays;
    }

    public Interval getMeetingTimes() {
        // ICC clubs always meet at the same times
        return meetings.get(0);
    }

}
