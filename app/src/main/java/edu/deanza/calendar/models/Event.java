package edu.deanza.calendar.models;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Created by Sara on 5/28/2016.
 */

public class Event {

    protected String name;
    protected String description;
    protected String location;
    protected String organizationName;
    protected DateTime start;
    protected DateTime end;

    // TODO: implement `categories` field

    public Event() {}

    public Event(String name, String description, String location, String organizationName,
                 String start, String end) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.organizationName = organizationName;
        this.start = DateTime.parse(start);
        this.end = DateTime.parse(end);
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