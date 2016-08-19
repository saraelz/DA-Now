package edu.deanza.calendar.models;

import org.joda.time.Interval;

import java.util.List;

/**
 * Created by soso1 on 8/8/2016.
 */

public class Organization {

    protected String name;
    protected String description;
    protected String location;
    protected String facebookUrl;
    protected List<Interval> meetings;

    public Organization() {}

    public Organization(String name, String description, String location, String facebookUrl, List<Interval> meetings) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.facebookUrl = facebookUrl;
        this.meetings = meetings;
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

}
