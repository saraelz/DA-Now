package edu.deanza.calendar.domain.models;

import org.joda.time.Interval;

import java.util.List;

/**
 * Created by soso1 on 8/8/2016.
 */

public class Organization {

    protected final String name;
    protected final String description;
    protected final String location;
    protected final String facebookUrl;
    protected final List<Interval> meetings;

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
