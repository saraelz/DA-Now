package edu.deanza.calendar.domain.models;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.deanza.calendar.domain.EventRepository;
import edu.deanza.calendar.domain.Interval;
import edu.deanza.calendar.domain.Subscribable;
import edu.deanza.calendar.util.Callback;

/**
 * Created by soso1 on 8/8/2016.
 */

public class Organization implements Subscribable, Serializable {

    protected final String name;
    protected final String description;
    protected final String location;
    protected final String facebookUrl;
    protected final List<Meeting> meetings;
    protected final EventRepository eventRepository;
    protected List<Event> events;
    protected OrganizationSubscription subscription;

    public Organization(String name, String description, String location, String facebookUrl,
                        List<Meeting> meetings, EventRepository eventRepository) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.facebookUrl = facebookUrl;
        this.meetings = meetings;
        this.eventRepository = eventRepository;
        this.events = null;
    }

    public Organization(String name, String description, String location, String facebookUrl,
                        List<Meeting> meetings, List<Event> events) {
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

    public List<Meeting> getMeetings() {
        return meetings;
    }

    public void getEvents(final Callback<Event> callback) {
        if (events == null) {
            assert eventRepository != null;
            events = new ArrayList<>();
            eventRepository.findByOrganization(name, new Callback<Event>() {
                @Override
                protected void call(Event data) {
                    events.add(data);
                    callback.run(data);
                }
            });
        }
        else {
            for (Event e : events) {
                callback.setArgument(e);
                callback.run();
            }
        }
    }

    @Override
    public OrganizationSubscription getSubscription() {
        return subscription;
    }

    @Override
    public void subscribe(Subscription subscription) {
        subscribe((OrganizationSubscription) subscription);
    }

    public void subscribe(OrganizationSubscription subscription) {
        this.subscription = subscription;
    }

    @Override
    public void unsubscribe() {
        this.subscription = null;
    }

    @Override
    public String getKey() {
        return name;
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

    public static final class Meeting implements Interval, Subscribable {

        private final String key;
        private final DateTime start;
        private final DateTime end;
        private Subscription subscription;

        public Meeting(String key, DateTime start, DateTime end) {
            this.key = key;
            this.start = start;
            this.end = end;
        }

        @Override
        public DateTime getStart() {
            return start;
        }

        @Override
        public DateTime getEnd() {
            return end;
        }

        @Override
        public Subscription getSubscription() {
            return subscription;
        }

        @Override
        public void subscribe(Subscription subscription) {
            this.subscription = subscription;
        }

        @Override
        public void unsubscribe() {
            this.subscription = null;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Meeting meeting = (Meeting) o;

            return key.equals(meeting.key);

        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }
    }

}
