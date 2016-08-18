package edu.deanza.calendar.models;

/**
 * Created by soso1 on 8/8/2016.
 */

public class Organization {

    protected String name;
    protected String description;
    protected String location;
    protected String facebookUrl;

    public Organization() {}

    public Organization(String name, String description, String location, String facebookUrl) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.facebookUrl = facebookUrl;
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

}
