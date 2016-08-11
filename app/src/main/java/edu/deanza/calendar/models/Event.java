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
    protected DateTime startTime;
    protected DateTime endTime;

    // TODO: implement `categories` field

    public Event() {}

    public Event(String name, String description, String location, String organization,
                 String startTime, String endTime) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.organization = organization;
        this.startTime = DateTime.parse(startTime);
        this.endTime = DateTime.parse(endTime);
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

    public DateTime getStartTime() {
        return startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

}