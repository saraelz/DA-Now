package edu.deanza.calendar.dal;

import org.joda.time.DateTime;

import java.util.Map;

import edu.deanza.calendar.domain.OrganizationRepository;
import edu.deanza.calendar.domain.models.Event;

/**
 * Created by karinaantonio on 8/20/16.
 */

class EventMapper implements DataMapper<Event> {

    private final OrganizationRepository repository;

    public EventMapper(OrganizationRepository repository) {
        this.repository = repository;
    }

    public Event map(String eventKey, Map<Object, Object> rawEvent) {
        int nameStartDelimiter = eventKey.lastIndexOf('|');
        String name = eventKey.substring(nameStartDelimiter + 1, eventKey.length());

        String description = (String) rawEvent.get("description");
        String location = (String) rawEvent.get("location");
        String organizationName = (String) rawEvent.get("organizationName");
        DateTime start = DateTime.parse((String) rawEvent.get("start"));
        DateTime end = DateTime.parse((String) rawEvent.get("end"));

        return new Event(name, description, location, organizationName, start, end, repository);
    }

}
