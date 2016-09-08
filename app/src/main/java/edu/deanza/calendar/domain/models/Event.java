package edu.deanza.calendar.domain.models;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.util.ArrayList;
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
    protected final List<String> organizationNames;
    protected final DateTime start;
    protected final DateTime end;
    protected final OrganizationRepository organizationRepository;
    // If an Organization entry does not exist for a given organizationName, the List entry will be null
    protected List<Organization> organizations;

    // TODO: implement `categories` field

    public Event(String name, String description, String location, List<String> organizationNames,
                 DateTime start, DateTime end, OrganizationRepository organizationRepository) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.organizationNames = organizationNames;
        this.start = start;
        this.end = end;
        this.organizationRepository = organizationRepository;
    }

    public Event(String name, String description, String location, List<String> organizationNames,
                 DateTime start, DateTime end, List<Organization> organizations) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.organizationNames = organizationNames;
        this.start = start;
        this.end = end;
        this.organizations = organizations;
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

    public List<String> getOrganizationNames() {
        return organizationNames;
    }

    public void getOrganizations(final Callback<List<Organization>> callback) {
        if (organizations == null) {
            assert organizationRepository != null;
            organizationRepository.findByNames(organizationNames, new Callback<List<Organization>>() {
                @Override
                protected void call(List<Organization> data) {
                    organizations = data;
                    callback.setArgument(data);
                    callback.run();
                }
            });
        }
        else {
            callback.setArgument(organizations);
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