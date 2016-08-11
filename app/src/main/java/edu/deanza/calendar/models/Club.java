package edu.deanza.calendar.models;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

public class Club extends Organization {

    private List<LocalDate> meetings;
    private List<Day> meetingDays;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;

    public Club() {}

    public Club(String name, String description, String facebookUrl, List<String> meetings,
                List<String> meetingDays, String startTime, String endTime, String location) {
        super(name, description, facebookUrl);

        this.meetings = new ArrayList<LocalDate>();
        for (String s : meetings) {
            this.meetings.add(LocalDate.parse(s));
        }

        this.meetingDays = new ArrayList<Day>();
        for (String s : meetingDays) {
            this.meetingDays.add(Day.valueOf(s));
        }

        this.startTime = LocalTime.parse(startTime);
        this.endTime = LocalTime.parse(endTime);
        this.location = location;
    }

    public List<LocalDate> getMeetings() {
        return meetings;
    }

    public List<Day> getMeetingDays() {
        return meetingDays;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getLocation() {
        return location;
    }

}
