package edu.deanza.calendar.domain.models;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.util.List;

import edu.deanza.calendar.domain.OrganizationRepository;
import edu.deanza.calendar.util.Callback;

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
    protected final OrganizationRepository organizationRepository;
    protected Organization organization;

    // TODO: implement `categories` field

    public Event(String name, String description, String location, String organizationName,
                 DateTime start, DateTime end, OrganizationRepository organizationRepository) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.organizationName = organizationName;
        this.start = start;
        this.end = end;
        this.organizationRepository = organizationRepository;
    }

    public Event(String name, String description, String location, String organizationName,
                 DateTime start, DateTime end, Organization organization) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.organizationName = organizationName;
        this.start = start;
        this.end = end;
        this.organization = organization;
        this.organizationRepository = null;
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

    public void getOrganization(final Callback<Organization> callback) {
        if (organization == null) {
            assert organizationRepository != null;
            organizationRepository.findByName(organizationName, new Callback<Organization>() {
                @Override
                protected void call(Organization data) {
                    organization = data;
                    callback.setArgument(data);
                    callback.run();
                }
            });
        }
        else {
            callback.setArgument(organization);
            callback.run();
        }
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