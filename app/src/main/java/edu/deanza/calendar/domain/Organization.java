package edu.deanza.calendar.domain;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.ISODateTimeFormat;

import java.io.Serializable;
import java.util.List;

import edu.deanza.calendar.domain.interfaces.Subscribable;
import edu.deanza.calendar.domain.interfaces.SubscriptionDao;

/**
 * Created by soso1 on 8/8/2016.
 */

public class Organization implements Subscribable, Serializable {

    final String name;
    final String description;
    final String location;
    final String facebookUrl;
    final List<RegularMeeting> meetings;
    List<Event> events;
    OrganizationSubscription subscription;

    public Organization(String name, String description, String location, String facebookUrl,
                        List<RegularMeeting> meetings) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.facebookUrl = facebookUrl;
        this.meetings = meetings;
    }

    @Override
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

    public List<Event> getEvents() {
        return events;
    }

    @Override
    public OrganizationSubscription getSubscription() {
        return subscription;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @Override
    public void subscribe(Subscription subscription, SubscriptionDao dao) {
        this.subscription = (OrganizationSubscription) subscription;
        dao.add(this.subscription);
        if (this.subscription.isNotifyingEvents()) {
            for (Event e : events) {
                e.subscribe(subscription, dao);
            }
        }
        if (this.subscription.isNotifyingMeetings()) {
            for (RegularMeeting meeting : meetings) {
                meeting.subscribe(subscription, dao);
            }
        }
    }

    // TODO: option to unsubscribe from Org w/o removing all current subscriptions--just stop future ones
    @Override
    public void unsubscribe(final SubscriptionDao dao) {
        dao.remove(subscription);
        if (subscription.isNotifyingEvents()) {
            for (Event e : events) {
                e.unsubscribe(dao);
            }
        }
        if (subscription.isNotifyingMeetings()) {
            for (RegularMeeting meeting : meetings) {
                meeting.unsubscribe(dao);
            }
        }
        subscription = null;
    }

    @Override
    public boolean isSubscribed() {
        return subscription != null;
    }

    @Override
    public String getKey() {
        return name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Organization{");
        sb.append("name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", location='").append(location).append('\'');
        sb.append(", facebookUrl='").append(facebookUrl).append('\'');
        sb.append(", meetings=").append(meetings);
        sb.append(", subscription=").append(subscription);
        sb.append('}');
        return sb.toString();
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
        public String getName() {
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .appendLiteral(organizationName)
                    .appendLiteral(" meeting on ")
                    // DayOfWeek, Month day
                    .appendPattern("EEEE, MMMM d")
                    .toFormatter();
            return start.toString(formatter);
        }

        @Override
        public String getKey() {
            return start.toString(ISODateTimeFormat.yearMonthDay()) + '|' + organizationName;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("RegularMeeting{");
            sb.append("organizationName='").append(organizationName).append('\'');
            sb.append('}');
            return sb.toString();
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
