package edu.deanza.calendar.models;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Created by Sara on 5/28/2016.
 */

public class Event {

    protected final String name;
    protected final String description;
    protected final String location;
    protected final String organizationName;
    protected final DateTime start;
    protected final DateTime end;

    // TODO: implement `categories` field

    public Event(String name, String description, String location, String organizationName,
                DateTime start, DateTime end) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.organizationName = organizationName;
        this.start = start;
        this.end = end;
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

    public String getOrganizationName() {
        return organizationName;
    }

    public DateTime getStart() {
        return start;
    }

    public DateTime getEnd() {
        return end;
    }

    public String getKey() {
        return start.toString(ISODateTimeFormat.yearMonthDay()) + '|' + name;
    }

}