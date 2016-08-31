package edu.deanza.calendar.domain.models;

import java.util.List;

/**
 * Created by karinaantonio on 8/18/16.
 */

public abstract class OrganizationEvent {

    protected Organization organization;
    protected Event event;

    public OrganizationEvent(Organization organization) {
        this.organization = organization;
    }

    public OrganizationEvent(Event event) {
        this.event = event;
    }

    public Organization getOrganization() {
        return organization;
    }

    public Event getEvent() {
        return event;
    }

    public abstract List<Event> allEvents();

}
