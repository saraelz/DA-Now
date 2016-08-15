package edu.deanza.calendar.dal;

import org.joda.time.LocalDate;

import java.util.List;

import edu.deanza.calendar.models.Event;

/**
 * Created by karinaantonio on 8/11/16.
 */

public interface EventRepository {

    public List<Event> all();

    public List<Event> ofOrganization(String organization);

    public List<Event> on(LocalDate date);

    public List<Event> before(LocalDate date);

    public List<Event> after(LocalDate date);

    public List<Event> between(LocalDate dateA, LocalDate dateB);

}
