package edu.deanza.calendar.domain.models;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.deanza.calendar.domain.EventRepository;
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
    protected final List<RegularMeeting> meetings;
    protected final EventRepository eventRepository;
    protected List<Event> events;
    protected OrganizationSubscription subscription;

    public Organization(String name, String description, String location, String facebookUrl,
                        List<RegularMeeting> meetings, EventRepository eventRepository) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.facebookUrl = facebookUrl;
        this.meetings = meetings;
        this.eventRepository = eventRepository;
        this.events = null;
    }

    public Organization(String name, String description, String location, String facebookUrl,
                        List<RegularMeeting> meetings, List<Event> events) {
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

    public List<RegularMeeting> getMeetings() {
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
    public boolean isSubscribed() {
        return subscription == null;
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

    public static final class RegularMeeting extends Meeting implements Subscribable, Serializable {

        private final String organizationName;

        public RegularMeeting(DateTime start, DateTime end, String organizationName) {
            super(start, end);
            this.organizationName = organizationName;
        }

        public String getOrganizationName() {
            return organizationName;
        }

        @Override
        public String getKey() {
            return start.toString(ISODateTimeFormat.yearMonthDay()) + '|' + organizationName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            RegularMeeting meeting = (RegularMeeting) o;

            if (!organizationName.equals(meeting.organizationName)) {
                return false;
            }
            if (!start.equals(meeting.start)) {
                return false;
            }
            return end.equals(meeting.end);

        }

        @Override
        public int hashCode() {
            int result = organizationName.hashCode();
            result = 31 * result + start.hashCode();
            result = 31 * result + end.hashCode();
            return result;
        }
    }

}
