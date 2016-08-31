package edu.deanza.calendar.domain;

import org.joda.time.LocalDate;

import java.util.List;

import edu.deanza.calendar.domain.models.Event;

/**
 * Created by karinaantonio on 8/11/16.
 */

public interface EventRepository {

    public List<Event> all();

    public List<Event> findByOrganization(String organizationName);

    public List<Event> on(LocalDate date);

    public List<Event> before(LocalDate date);

    public List<Event> after(LocalDate date);

    public List<Event> between(LocalDate start, LocalDate end);

}
