package edu.deanza.calendar.domain.models;

import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.List;

import edu.deanza.calendar.domain.EventRepository;
import edu.deanza.calendar.util.Callback;

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

    public void getEvents(final Callback<List<Event>> callback) {
        if (events == null) {
            assert eventRepository != null;
            eventRepository.findByOrganization(name, new Callback<List<Event>>() {
                @Override
                protected void call(List<Event> data) {
                    events = data;
                    callback.setArgument(data);
                    callback.run();
                }
            });
        }
        else {
            callback.setArgument(events);
            callback.run();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Organization that = (Organization) o;

        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
