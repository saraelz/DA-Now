package edu.deanza.calendar.domain.models;

import java.io.Serializable;
import java.util.List;

import edu.deanza.calendar.domain.EventRepository;
import edu.deanza.calendar.domain.Subscribable;

public class Club extends Organization implements Subscribable, Serializable {

    private final List<Day> meetingDays;

    public Club(String name, String description, String location, String facebookUrl,
                List<RegularMeeting> meetings, EventRepository eventRepository, List<Day> meetingDays) {
        super(name, description, location, facebookUrl, meetings, eventRepository);
        this.meetingDays = meetingDays;
    }

    public Club(String name, String description, String location, String facebookUrl,
                List<RegularMeeting> meetings, List<Event> events, List<Day> meetingDays) {
        super(name, description, location, facebookUrl, meetings, events);
        this.meetingDays = meetingDays;
    }

    public List<Day> getMeetingDays() {
        return meetingDays;
    }

}
