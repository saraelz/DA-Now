package edu.deanza.calendar.domain.models;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.deanza.calendar.domain.OrganizationRepository;
import edu.deanza.calendar.domain.Subscribable;
import edu.deanza.calendar.util.Callback;

/**
 * Created by Sara on 5/28/2016.
 */

public class Event extends Meeting implements Subscribable, Serializable {

    protected final String name;
    protected final String description;
    protected final String location;
    protected final List<String> organizationNames;
    protected final OrganizationRepository organizationRepository;
    // If an Organization entry does not exist for a given organizationName, the List entry will be null
    protected List<Organization> organizations;

    // TODO: implement `categories` field

    public Event(DateTime start, DateTime end, String name, String description, String location, List<String> organizationNames, OrganizationRepository organizationRepository) {
        super(start, end);
        this.name = name;
        this.description = description;
        this.location = location;
        this.organizationNames = organizationNames;
        this.organizationRepository = organizationRepository;
        this.organizations = null;
    }

    public Event(DateTime start, DateTime end, String name, String description, String location, List<String> organizationNames, List<Organization> organizations) {
        super(start, end);
        this.name = name;
        this.description = description;
        this.location = location;
        this.organizationNames = organizationNames;
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

    public void getOrganizations(final Callback<Organization> callback) {
        if (organizations == null) {
            assert organizationRepository != null;
            organizations = new ArrayList<>();
            for (String name : organizationNames) {
                organizationRepository.findByName(name, new Callback<Organization>() {
                    @Override
                    protected void call(Organization data) {
                        organizations.add(data);
                        callback.run(data);
                    }
                });
            }
        }
        else {
            for (Organization organization : organizations) {
                callback.setArgument(organization);
                callback.run();
            }
        }
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