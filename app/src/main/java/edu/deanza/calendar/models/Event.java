package edu.deanza.calendar.models;

import org.joda.time.DateTime;

/**
 * Created by Sara on 5/28/2016.
 */

public class Event {

    protected String name;
    protected String description;
    protected String location;
    protected String organization;
    protected DateTime start;
    protected DateTime end;

    // TODO: implement `categories` field

    public Event() {}

    public Event(String name, String description, String location, String organization,
                 String start, String end) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.organization = organization;
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

    public String getOrganization() {
        return organization;
    }

    public DateTime getStart() {
        return start;
    }

    public DateTime getEnd() {
        return end;
    }

}