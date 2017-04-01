package edu.deanza.calendar.domain;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.io.Serializable;
import java.util.List;

import edu.deanza.calendar.domain.interfaces.Subscribable;

/**
 * Created by Sara on 5/28/2016.
 */

public class Event extends Meeting implements Subscribable, Serializable {

    final String name;
    final String description;
    final String location;
    final List<String> organizationNames;
    // If an Organization entry does not exist for a given organizationName, the List entry will be null

    // TODO: implement `categories` field


    public Event(DateTime start, DateTime end, String name, String description, String location,
                 List<String> organizationNames) {
        super(start, end);
        this.name = name;
        this.description = description;
        this.location = location;
        this.organizationNames = organizationNames;
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

    public List<String> getOrganizationNames() {
        return organizationNames;
    }

    public boolean isAllDay() {
        return start.equals(end);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Event{");
        sb.append("name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", location='").append(location).append('\'');
        sb.append(", organizationNames=").append(organizationNames);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public String getKey() {
        return start.toString(ISODateTimeFormat.yearMonthDay()) + '|' + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Event event = (Event) o;

        if (!name.equals(event.name)) {
            return false;
        }
        return start.equals(event.start);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + start.hashCode();
        return result;
    }
}