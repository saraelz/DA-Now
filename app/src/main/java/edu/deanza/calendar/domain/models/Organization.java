package edu.deanza.calendar.domain.models;

import org.joda.time.Interval;

import java.util.List;

import edu.deanza.calendar.domain.EventRepository;

/**
 * Created by soso1 on 8/8/2016.
 */

public class Organization {

    protected final String name;
    protected final String description;
    protected final String location;
    protected final String facebookUrl;
    protected final List<Interval> meetings;
    protected final EventRepository eventRepository;
    protected List<Event> events;

    public Organization(String name, String description, String location, String facebookUrl,
                        List<Interval> meetings, EventRepository eventRepository) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.facebookUrl = facebookUrl;
        this.meetings = meetings;
        this.eventRepository = eventRepository;
    }

    public Organization(String name, String description, String location, String facebookUrl,
                        List<Interval> meetings, List<Event> events) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.facebookUrl = facebookUrl;
        this.meetings = meetings;
        this.events = events;
        this.eventRepository = null;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public List<Interval> getMeetings() {
        return meetings;
    }

    public List<Event> getEvents() {
        if (events == null) {
            assert eventRepository != null;
            events = eventRepository.findByOrganization(name);
        }
        return events;

    }

}
