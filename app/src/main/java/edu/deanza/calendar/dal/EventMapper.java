package edu.deanza.calendar.dal;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.deanza.calendar.domain.OrganizationRepository;
import edu.deanza.calendar.domain.models.Event;

/**
 * Created by karinaantonio on 8/20/16.
 */

class EventMapper implements DataMapper<Event>, Serializable {

    private final OrganizationRepository repository;

    public EventMapper(OrganizationRepository repository) {
        this.repository = repository;
    }

    public Event map(String eventKey, Map<Object, Object> rawEvent) {
        int nameStartDelimiter = eventKey.lastIndexOf('|');
        String name = eventKey.substring(nameStartDelimiter + 1, eventKey.length());

        String description = (String) rawEvent.remove("description");
        String location = (String) rawEvent.remove("location");
        DateTime start = DateTime.parse((String) rawEvent.remove("start"));
        DateTime end = DateTime.parse((String) rawEvent.remove("end"));

        List<String> organizationNames = new ArrayList<>();
        for (Map.Entry<Object, Object> entry: rawEvent.entrySet()) {
            // Only organization entries remain in rawEvent
            organizationNames.add((String) entry.getKey());
        }

        return new Event(start, end, name, description, location, organizationNames, repository);
    }

}
